/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cr.ac.ucr.connexion_backend.service;

import java.util.List;
import cr.ac.ucr.connexion_backend.model.entities.Issue;
import cr.ac.ucr.connexion_backend.model.entities.IssueComment;
import cr.ac.ucr.connexion_backend.model.entities.IssueNote;

/**
 *
 * @author sebas
 */
public interface IssueService {

    Issue registerIssue(Issue issue, int clientId);

    List<Issue> findByClientId(int clientId);

    List<Issue> findHistoryByClientId(int clientId);

    List<Issue> findAllOrderByTimestamp();

    Issue findById(int issueId);

    Issue assignSupporter(int issueId, int supportUserId);

    Issue startProgress(int issueId);

    Issue resolveIssue(int issueId, String resolutionComment);

    List<IssueComment> findCommentsByIssueId(int issueId);

    IssueComment addComment(int issueId, int clientId, String commentText);

    IssueComment addSupportComment(int issueId, int supporterId, String commentText);

    List<IssueNote> findNotesByIssueId(int issueId);        

    IssueNote addNote(int issueId, int supporterId, String description); 
}
