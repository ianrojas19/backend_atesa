/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.Issue;
import cr.ac.ucr.connexion_backend.model.entities.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 *
 * @author sebas
 */
public interface IssueRepository extends JpaRepository<Issue, Integer> {

    List<Issue> findByClientId(int clientId);

    List<Issue> findByStatus(IssueStatus status);

    List<Issue> findByClientIdOrderByRegisterTimestampDesc(int clientId);

    // CU10 — todas las solicitudes en orden ascendente por fecha
    List<Issue> findAllByOrderByRegisterTimestampAsc();
}
