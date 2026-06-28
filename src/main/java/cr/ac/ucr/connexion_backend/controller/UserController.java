package cr.ac.ucr.connexion_backend.controller;

import cr.ac.ucr.connexion_backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request.email(), request.password())
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(Map.of(
                        "id",       user.id(),
                        "email",    user.email(),
                        "fullName", user.fullName()
                )))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Correo electronico o contrasena incorrectos.")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        boolean created = userService.register(
                request.name(),
                request.firstSurname(),
                request.secondSurname(),
                request.email(),
                request.password(),
                request.address(),
                request.phone()
        );

        if (!created) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Ya existe una cuenta con ese correo electrónico."));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Cliente registrado exitosamente."));
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {
    }

    public record RegisterRequest(
            @NotBlank String name,
            @NotBlank String firstSurname,
            @NotBlank String secondSurname,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6) String password,
            @NotBlank String address,
            int phone
    ) {
    }
}
