package cr.ac.ucr.connexion_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private StateWebSocketHandler stateWebSocketHandler;
    private ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(StateWebSocketHandler stateWebSocketHandler, ChatWebSocketHandler chatWebSocketHandler) {
        this.stateWebSocketHandler = stateWebSocketHandler;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(stateWebSocketHandler, "/ws/status")
                .setAllowedOrigins("*");
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .setAllowedOrigins("*");
    }
}
