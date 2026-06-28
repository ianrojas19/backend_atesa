package cr.ac.ucr.connexion_backend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_log")
public class IssueLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "issue_id")
    private Integer issueId;

    @Column(name = "supervisor_id")
    private Integer supervisorId;

    @Column(name = "supporter_id")
    private Integer supporterId;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "log_timestamp", nullable = false)
    private LocalDateTime logTimestamp;

    public IssueLog() {
    }

    public IssueLog(Integer issueId, Integer supervisorId, Integer supporterId, String description, LocalDateTime logTimestamp) {
        this.issueId = issueId;
        this.supervisorId = supervisorId;
        this.supporterId = supporterId;
        this.description = description;
        this.logTimestamp = logTimestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Integer supervisorId) {
        this.supervisorId = supervisorId;
    }

    public Integer getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(Integer supporterId) {
        this.supporterId = supporterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLogTimestamp() {
        return logTimestamp;
    }

    public void setLogTimestamp(LocalDateTime logTimestamp) {
        this.logTimestamp = logTimestamp;
    }
}
