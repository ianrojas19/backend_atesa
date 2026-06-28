package cr.ac.ucr.connexion_backend.service;

import cr.ac.ucr.connexion_backend.model.entities.IssueLog;
import java.util.List;

public interface IssueLogService {
    void logAction(Integer issueId, Integer supervisorId, Integer supporterId, String description);
    List<IssueLog> getAllLogs();
    void deleteAllLogs();
}
