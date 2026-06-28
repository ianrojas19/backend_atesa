package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.IssueComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueCommentRepository extends JpaRepository<IssueComment, Integer> {

    List<IssueComment> findByIssueIdOrderByCreatedAtAsc(int issueId);
}
