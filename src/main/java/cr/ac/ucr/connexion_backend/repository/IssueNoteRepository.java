/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.IssueNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author sebas
 */
public interface IssueNoteRepository extends JpaRepository<IssueNote, Integer> {

    List<IssueNote> findByIssueIdOrderByNoteTimestampAsc(int issueId);
}
