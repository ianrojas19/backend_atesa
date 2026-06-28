package cr.ac.ucr.connexion_backend.controller;

import cr.ac.ucr.connexion_backend.model.entities.IssueLog;
import cr.ac.ucr.connexion_backend.service.IssueLogService;
import cr.ac.ucr.connexion_backend.repository.SupervisorRepository;
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
    private final SupervisorRepository supervisorRepository;

    @Autowired
    public IssueLogController(IssueLogService issueLogService, SupervisorRepository supervisorRepository) {
        this.issueLogService = issueLogService;
        this.supervisorRepository = supervisorRepository;
    }

    @GetMapping
    public ResponseEntity<?> getLogs(
            @RequestParam(required = false) Integer actorId,
            @RequestParam(required = false) String actorRole) {
        if (!"supervisor".equalsIgnoreCase(actorRole) || actorId == null || !supervisorRepository.existsById(actorId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Solo los supervisores autorizados pueden ver las bitácoras."));
        }
        return ResponseEntity.ok(issueLogService.getAllLogs());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllLogs(
            @RequestParam(required = false) Integer actorId,
            @RequestParam(required = false) String actorRole) {
        if (!"supervisor".equalsIgnoreCase(actorRole) || actorId == null || !supervisorRepository.existsById(actorId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Solo los supervisores autorizados pueden eliminar las bitácoras."));
        }
        issueLogService.deleteAllLogs();
        return ResponseEntity.ok(Map.of("message", "Todas las bitácoras han sido eliminadas."));
    }
}
