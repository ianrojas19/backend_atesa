package cr.ac.ucr.connexion_backend.service;

import java.util.Optional;
import cr.ac.ucr.connexion_backend.model.entities.User;
import cr.ac.ucr.connexion_backend.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<AuthenticatedUser> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .map(user -> new AuthenticatedUser(user.getId(), user.getEmail(), user.getFullName()));
    }

    @Override
    public boolean register(String name, String firstSurname, String secondSurname, String email, String password, String address, int phone) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        try {
            User user = new User(name, firstSurname, secondSurname, email, password, address, phone);
            userRepository.save(user);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }
}
