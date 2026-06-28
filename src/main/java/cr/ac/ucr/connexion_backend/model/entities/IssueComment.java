package cr.ac.ucr.connexion_backend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_comments")
public class IssueComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "issue_id")
    private int issueId;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "supporter_id")
    private Integer supporterId;

    @Column(name = "author_type")
    private String authorType;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public IssueComment() {
    }

    public IssueComment(int issueId, int clientId, String commentText, LocalDateTime createdAt) {
        this.issueId = issueId;
        this.clientId = clientId;
        this.authorType = "CLIENT";
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public IssueComment(int issueId, Integer clientId, Integer supporterId, String authorType, String commentText, LocalDateTime createdAt) {
        this.issueId = issueId;
        this.clientId = clientId;
        this.supporterId = supporterId;
        this.authorType = authorType;
        this.commentText = commentText;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(Integer supporterId) {
        this.supporterId = supporterId;
    }

    public String getAuthorType() {
        return authorType;
    }

    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
