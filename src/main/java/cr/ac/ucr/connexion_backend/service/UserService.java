package cr.ac.ucr.connexion_backend.service;

import java.util.Optional;

public interface UserService {

    Optional<AuthenticatedUser> login(String email, String password);

    boolean register(String name, String firstSurname, String secondSurname, String email, String password, String address, int phone);

    record AuthenticatedUser(Integer id, String email, String fullName) {
    }
}
