package cr.ac.ucr.connexion_backend.service;

import cr.ac.ucr.connexion_backend.model.entities.IssueLog;
import cr.ac.ucr.connexion_backend.repository.IssueLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueLogServiceImpl implements IssueLogService {

    private final IssueLogRepository issueLogRepository;

    @Autowired
    public IssueLogServiceImpl(IssueLogRepository issueLogRepository) {
        this.issueLogRepository = issueLogRepository;
    }

    @Override
    public void logAction(Integer issueId, Integer supervisorId, Integer supporterId, String description) {
        if (supervisorId == null && supporterId == null) {
            throw new IllegalArgumentException("Se requiere un supervisorId o supporterId para registrar la bitácora.");
        }
        
        IssueLog log = new IssueLog(issueId, supervisorId, supporterId, description, LocalDateTime.now());
        issueLogRepository.save(log);
    }

    @Override
    public List<IssueLog> getAllLogs() {
        return issueLogRepository.findAllByOrderByLogTimestampDesc();
    }

    @Override
    public void deleteAllLogs() {
        issueLogRepository.deleteAll();
    }
}
