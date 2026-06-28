package cr.ac.ucr.connexion_backend.controller;

import cr.ac.ucr.connexion_backend.model.entities.Issue;
import cr.ac.ucr.connexion_backend.model.entities.IssueComment;
import cr.ac.ucr.connexion_backend.model.entities.IssueNote;
import cr.ac.ucr.connexion_backend.service.IssueService;
import cr.ac.ucr.connexion_backend.service.IssueLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueLogService issueLogService;

    @PostMapping
    public ResponseEntity<Issue> registerIssue(
            @RequestBody Issue issue,
            @RequestParam int clientId) {
        return new ResponseEntity<>(issueService.registerIssue(issue, clientId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues() {
        return ResponseEntity.ok(issueService.findAllOrderByTimestamp());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getById(@PathVariable int id) {
        return ResponseEntity.ok(issueService.findById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Issue>> getByClient(@PathVariable int clientId) {
        return ResponseEntity.ok(issueService.findByClientId(clientId));
    }

    @GetMapping("/client/{clientId}/history")
    public ResponseEntity<List<Issue>> getHistoryByClient(@PathVariable int clientId) {
        return ResponseEntity.ok(issueService.findHistoryByClientId(clientId));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Issue> assignSupporter(
            @PathVariable int id,
            @RequestParam int supportUserId,
            @RequestParam(required = false) Integer actorId,
            @RequestParam(required = false) String actorRole) {
        
        Issue issue = issueService.assignSupporter(id, supportUserId);
        
        Integer supervisorId = "supervisor".equals(actorRole) ? actorId : null;
        Integer supporterId = "supporter".equals(actorRole) ? actorId : null;
        if (supervisorId != null || supporterId != null) {
            issueLogService.logAction(id, supervisorId, supporterId, "Se asignó la solicitud al soportista ID: " + supportUserId);
        }
        
        return ResponseEntity.ok(issue);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Issue> startProgress(
            @PathVariable int id,
            @RequestParam(required = false) Integer actorId,
            @RequestParam(required = false) String actorRole) {
        
        Issue issue = issueService.startProgress(id);
        
        Integer supervisorId = "supervisor".equals(actorRole) ? actorId : null;
        Integer supporterId = "supporter".equals(actorRole) ? actorId : null;
        if (supervisorId != null || supporterId != null) {
            issueLogService.logAction(id, supervisorId, supporterId, "Se inició el progreso de la solicitud");
        }
        
        return ResponseEntity.ok(issue);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<Issue> resolveIssue(
            @PathVariable int id,
            @RequestParam String resolutionComment,
            @RequestParam(required = false) Integer actorId,
            @RequestParam(required = false) String actorRole) {
        
        Issue issue = issueService.resolveIssue(id, resolutionComment);
        
        Integer supervisorId = "supervisor".equals(actorRole) ? actorId : null;
        Integer supporterId = "supporter".equals(actorRole) ? actorId : null;
        if (supervisorId != null || supporterId != null) {
            issueLogService.logAction(id, supervisorId, supporterId, "Se resolvió la solicitud: " + resolutionComment);
        }
        
        return ResponseEntity.ok(issue);
    }

    @GetMapping("/{id}/notes")
    public ResponseEntity<List<IssueNote>> getNotes(@PathVariable int id) {
        return ResponseEntity.ok(issueService.findNotesByIssueId(id));
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<IssueNote> addNote(
            @PathVariable int id,
            @RequestParam int supporterId,
            @RequestBody NoteRequest request) {
        
        IssueNote note = issueService.addNote(id, supporterId, request.description());
        issueLogService.logAction(id, null, supporterId, "Se agregó una nota técnica: " + request.description());
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<IssueComment>> getComments(@PathVariable int id) {
        return ResponseEntity.ok(issueService.findCommentsByIssueId(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<IssueComment> addComment(
            @PathVariable int id,
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) Integer supporterId,
            @RequestBody CommentRequest request) {
        if (supporterId != null) {
            IssueComment comment = issueService.addSupportComment(id, supporterId, request.commentText());
            issueLogService.logAction(id, null, supporterId, "Soporte agregó un comentario: " + request.commentText());
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        }
        if (clientId == null) {
            throw new IllegalArgumentException("Debe indicar clientId o supporterId.");
        }
        return new ResponseEntity<>(
                issueService.addComment(id, clientId, request.commentText()),
                HttpStatus.CREATED
        );
    }

    public record NoteRequest(String description) {

    }

    public record CommentRequest(String commentText) {

    }
}
