package cr.ac.ucr.connexion_backend.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cr.ac.ucr.connexion_backend.model.entities.ChatMessage;
import cr.ac.ucr.connexion_backend.model.entities.Issue;
import cr.ac.ucr.connexion_backend.repository.ChatMessageRepository;
import cr.ac.ucr.connexion_backend.repository.IssueRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatMessageRepository chatMessageRepository;
    private final IssueRepository issueRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Maps issueId to a list of WebSocket sessions for that issue
    private final Map<Integer, List<WebSocketSession>> issueSessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(@org.springframework.context.annotation.Lazy ChatMessageRepository chatMessageRepository, 
                                @org.springframework.context.annotation.Lazy IssueRepository issueRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // We wait for the initial AUTH message to assign it to an issue.
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer issueId = (Integer) session.getAttributes().get("issueId");
        if (issueId != null) {
            List<WebSocketSession> sessions = issueSessions.get(issueId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    issueSessions.remove(issueId);
                }
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode payload = objectMapper.readTree(message.getPayload());
        String type = payload.has("type") ? payload.get("type").asText() : "";

        if ("AUTH".equals(type)) {
            handleAuthMessage(session, payload);
        } else if ("CHAT".equals(type)) {
            handleChatMessage(session, payload);
        } else {
            session.sendMessage(new TextMessage("{\"error\":\"Invalid message type\"}"));
        }
    }

    private void handleAuthMessage(WebSocketSession session, JsonNode payload) throws IOException {
        int issueId = payload.get("issueId").asInt();
        int userId = payload.get("userId").asInt();
        String role = payload.get("role").asText(); // "CLIENT" or "SUPPORTER"

        Optional<Issue> issueOpt = issueRepository.findById(issueId);
        if (issueOpt.isEmpty()) {
            session.sendMessage(new TextMessage("{\"error\":\"Issue not found\"}"));
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        Issue issue = issueOpt.get();
        boolean authorized = false;

        if ("CLIENT".equals(role) && issue.getClientId() != null && issue.getClientId() == userId) {
            authorized = true;
        } else if ("SUPPORTER".equals(role) && issue.getSupporterId() != null && issue.getSupporterId() == userId) {
            authorized = true;
        } else if ("supervisor".equalsIgnoreCase(role)) {
            // Let supervisors view any chat if needed, assuming role string might be "supervisor"
            authorized = true;
        }

        if (!authorized) {
            session.sendMessage(new TextMessage("{\"error\":\"Unauthorized to access this chat\"}"));
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        // Save attributes to session
        session.getAttributes().put("issueId", issueId);
        session.getAttributes().put("userId", userId);
        session.getAttributes().put("role", role);

        issueSessions.computeIfAbsent(issueId, k -> new CopyOnWriteArrayList<>()).add(session);
        session.sendMessage(new TextMessage("{\"type\":\"AUTH_SUCCESS\"}"));
    }

    private void handleChatMessage(WebSocketSession session, JsonNode payload) throws IOException {
        Integer issueId = (Integer) session.getAttributes().get("issueId");
        Integer userId = (Integer) session.getAttributes().get("userId");
        String role = (String) session.getAttributes().get("role");

        if (issueId == null || userId == null || role == null) {
            session.sendMessage(new TextMessage("{\"error\":\"Not authenticated\"}"));
            return;
        }

        String text = payload.get("message").asText();
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) return;

        LocalDateTime now = LocalDateTime.now();
        ChatMessage chatMessage = new ChatMessage(issue, userId, role.toUpperCase(), text.trim(), now);
        chatMessage = chatMessageRepository.save(chatMessage);

        // Broadcast to all sessions in the same issue
        ObjectNode responsePayload = objectMapper.createObjectNode();
        responsePayload.put("type", "CHAT");
        responsePayload.put("id", chatMessage.getId());
        responsePayload.put("issueId", issueId);
        responsePayload.put("senderId", userId);
        responsePayload.put("senderRole", role.toUpperCase());
        responsePayload.put("message", chatMessage.getMessage());
        responsePayload.put("timestamp", chatMessage.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        TextMessage broadcastMessage = new TextMessage(objectMapper.writeValueAsString(responsePayload));
        
        List<WebSocketSession> sessions = issueSessions.get(issueId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(broadcastMessage);
                }
            }
        }
    }
}
