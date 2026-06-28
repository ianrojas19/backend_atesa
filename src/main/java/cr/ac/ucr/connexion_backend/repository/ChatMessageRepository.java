package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByIssue_IdOrderByTimestampAsc(int issueId);
}
