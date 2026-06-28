/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.connexion_backend.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author sebas
 */
@Entity
@Table(name = "issue_notes")
public class IssueNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "issue_id", nullable = false)
    private int issueId;

    @Column(name = "supporter_id", nullable = false)
    private int supporterId;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "note_timestamp", nullable = false)
    private LocalDateTime noteTimestamp;

    public IssueNote() {
    }

    public IssueNote(int issueId, int supporterId, String description, LocalDateTime noteTimestamp) {
        this.issueId = issueId;
        this.supporterId = supporterId;
        this.description = description;
        this.noteTimestamp = noteTimestamp;
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

    public int getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(int supporterId) {
        this.supporterId = supporterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getNoteTimestamp() {
        return noteTimestamp;
    }

    public void setNoteTimestamp(LocalDateTime noteTimestamp) {
        this.noteTimestamp = noteTimestamp;
    }
}
