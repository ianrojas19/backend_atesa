/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.connexion_backend.service;

import cr.ac.ucr.connexion_backend.config.StateWebSocketHandler;
import cr.ac.ucr.connexion_backend.model.entities.Issue;
import cr.ac.ucr.connexion_backend.model.entities.IssueComment;
import cr.ac.ucr.connexion_backend.model.entities.IssueNote;
import cr.ac.ucr.connexion_backend.model.entities.IssueStatus;
import cr.ac.ucr.connexion_backend.repository.IssueCommentRepository;
import cr.ac.ucr.connexion_backend.repository.IssueNoteRepository;
import cr.ac.ucr.connexion_backend.repository.IssueRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sebas
 */
@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private IssueCommentRepository issueCommentRepository;
    @Autowired
    private IssueNoteRepository issueNoteRepository;
    @Autowired
    private StateWebSocketHandler stateWebSocketHandler;

    @Override
    public Issue registerIssue(Issue issue, int clientId) {
        issue.setClientId(clientId);
        issue.setRegisterTimestamp(LocalDateTime.now());
        issue.setStatus(IssueStatus.REGISTERED);
        return issueRepository.save(issue);
    }

    @Override
    public List<Issue> findByClientId(int clientId) {
        return issueRepository.findByClientId(clientId);
    }

    @Override
    public List<Issue> findHistoryByClientId(int clientId) {
        return issueRepository.findByClientIdOrderByRegisterTimestampDesc(clientId);
    }

    @Override
    public List<Issue> findAllOrderByTimestamp() {
        return issueRepository.findAllByOrderByRegisterTimestampAsc();
    }

    @Override
    public Issue findById(int issueId) {
        return issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada: " + issueId));
    }

    @Override
    public Issue assignSupporter(int issueId, int supportUserId) {
        Issue issue = findById(issueId);
        if (issue.getStatus() != IssueStatus.REGISTERED) {
            throw new IllegalStateException("La solicitud debe estar en estado Ingresado (REGISTERED). Estado actual: " + issue.getStatus());
        }
        issue.setSupporterId(supportUserId);
        issue.setStatus(IssueStatus.ASSIGNED);

        Issue updatedIssue = issueRepository.save(issue);
        stateWebSocketHandler.broadcastStatusUpdate(updatedIssue.getId(), updatedIssue.getStatus().name());
        return updatedIssue;
    }

    @Override
    public Issue startProgress(int issueId) {
        Issue issue = findById(issueId);
        if (issue.getStatus() != IssueStatus.ASSIGNED) {
            throw new IllegalStateException("La solicitud debe estar ASSIGNED. Estado actual: " + issue.getStatus());
        }
        issue.setStatus(IssueStatus.IN_PROGRESS);

        Issue updatedIssue = issueRepository.save(issue);
        stateWebSocketHandler.broadcastStatusUpdate(updatedIssue.getId(), updatedIssue.getStatus().name());
        return updatedIssue;
    }

    @Override
    public Issue resolveIssue(int issueId, String resolutionComment) {
        Issue issue = findById(issueId);
        if (issue.getStatus() != IssueStatus.IN_PROGRESS) {
            throw new IllegalStateException("La solicitud debe estar IN_PROGRESS. Estado actual: " + issue.getStatus());
        }

        issue.setStatus(IssueStatus.RESOLVED);

        Issue updatedIssue = issueRepository.save(issue);
        stateWebSocketHandler.broadcastStatusUpdate(updatedIssue.getId(), updatedIssue.getStatus().name());
        return updatedIssue;
    }

    @Override
    public List<IssueNote> findNotesByIssueId(int issueId) {
        findById(issueId);
        return issueNoteRepository.findByIssueIdOrderByNoteTimestampAsc(issueId);
    }

    @Override
    public IssueNote addNote(int issueId, int supporterId, String description) {
        findById(issueId);
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la nota es obligatoria.");
        }
        IssueNote note = new IssueNote(issueId, supporterId, description.trim(), LocalDateTime.now());
        return issueNoteRepository.save(note);
    }

    @Override
    public List<IssueComment> findCommentsByIssueId(int issueId) {
        findById(issueId);
        return issueCommentRepository.findByIssueIdOrderByCreatedAtAsc(issueId);
    }

    @Override
    public IssueComment addComment(int issueId, int clientId, String commentText) {
        findById(issueId);
        if (commentText == null || commentText.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario es obligatorio.");
        }
        IssueComment comment = new IssueComment(issueId, clientId, null, "CLIENT", commentText.trim(), LocalDateTime.now());
        return issueCommentRepository.save(comment);
    }

    @Override
    public IssueComment addSupportComment(int issueId, int supporterId, String commentText) {
        Issue issue = findById(issueId);
        if (commentText == null || commentText.trim().isEmpty()) {
            throw new IllegalArgumentException("El comentario es obligatorio.");
        }
        IssueComment comment = new IssueComment(issueId, issue.getClientId(), supporterId, "SUPPORT", commentText.trim(), LocalDateTime.now());
        return issueCommentRepository.save(comment);
    }
}
