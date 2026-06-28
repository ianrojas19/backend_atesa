package cr.ac.ucr.connexion_backend.service;

import cr.ac.ucr.connexion_backend.model.entities.Supporter;
import java.util.List;
import java.util.Optional;

public interface SupportUserService {

    boolean register(
            String name,
            String firstSurname,
            String secondSurname,
            String email,
            String password,
            boolean supervisor,
            List<Integer> serviceIds
    );

    Optional<AuthenticatedSupportUser> login(String email, String password);

    List<Supporter> findAll();

    record AuthenticatedSupportUser(Integer id, String email, String fullName, String role) {

    }
}
