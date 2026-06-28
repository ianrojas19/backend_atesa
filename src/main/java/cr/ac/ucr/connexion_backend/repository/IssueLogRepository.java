package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.IssueLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueLogRepository extends JpaRepository<IssueLog, Integer> {
    List<IssueLog> findAllByOrderByLogTimestampDesc();
}
