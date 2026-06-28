package cr.ac.ucr.connexion_backend.repository;

import cr.ac.ucr.connexion_backend.model.entities.Supervisor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, Integer> {

    boolean existsByEmail(String email);

    Optional<Supervisor> findByEmail(String email);
}
