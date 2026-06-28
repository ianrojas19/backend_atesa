package cr.ac.ucr.connexion_backend.controller;

import cr.ac.ucr.connexion_backend.model.entities.IssueLog;
import cr.ac.ucr.connexion_backend.service.IssueLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class IssueLogController {

    private final IssueLogService issueLogService;

    @Autowired
    public IssueLogController(IssueLogService issueLogService) {
        this.issueLogService = issueLogService;
    }

    @GetMapping
    public ResponseEntity<List<IssueLog>> getLogs() {
        return ResponseEntity.ok(issueLogService.getAllLogs());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllLogs() {
        issueLogService.deleteAllLogs();
        return ResponseEntity.ok(Map.of("message", "Todas las bitácoras han sido eliminadas."));
    }
}
