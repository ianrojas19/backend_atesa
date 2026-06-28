package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.Supporter;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupporterRepository extends JpaRepository<Supporter, Integer> {

    boolean existsByEmail(String email);

    Optional<Supporter> findByEmail(String email);
}
