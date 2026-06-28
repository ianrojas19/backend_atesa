package cr.ac.ucr.connexion_backend.controller;

import cr.ac.ucr.connexion_backend.service.SupportUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support-users")
@CrossOrigin(origins = "*")
public class SupportUserController {

    private final SupportUserService supportUserService;
    private final cr.ac.ucr.connexion_backend.service.IssueLogService issueLogService;

    public SupportUserController(SupportUserService supportUserService, cr.ac.ucr.connexion_backend.service.IssueLogService issueLogService) {
        this.supportUserService = supportUserService;
        this.issueLogService = issueLogService;
    }

    /**
     * POST /api/support-users/login — Autenticar usuario de soporte
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginSupportRequest request) {
        return supportUserService.login(request.email(), request.password())
                .<ResponseEntity<?>>map(user -> {
                    Integer supervisorId = "supervisor".equals(user.role()) ? user.id() : null;
                    Integer supporterId = "supporter".equals(user.role()) ? user.id() : null;
                    issueLogService.logAction(null, supervisorId, supporterId, "Inicio de sesión");

                    return ResponseEntity.ok(Map.of(
                            "id", user.id(),
                            "email", user.email(),
                            "fullName", user.fullName(),
                            "role", user.role()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Correo electronico o contrasena incorrectos.")));
    }

    /**
     * POST /api/support-users/logout — Registrar cierre de sesión
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        Integer supervisorId = "supervisor".equals(request.role()) ? request.id() : null;
        Integer supporterId = "supporter".equals(request.role()) ? request.id() : null;
        issueLogService.logAction(null, supervisorId, supporterId, "Cierre de sesión");
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente."));
    }

    /**
     * POST /api/support-users/register — Registrar nuevo usuario de soporte
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterSupportUserRequest request) {
        try {
            boolean created = supportUserService.register(
                    request.name(),
                    request.firstSurname(),
                    request.secondSurname(),
                    request.email(),
                    request.password(),
                    request.supervisor(),
                    request.serviceIds()
            );

            if (!created) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Ya existe un usuario de soporte con ese correo electrónico."));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuario de soporte registrado exitosamente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    /**
     * GET /api/support-users — Obtener todos los usuarios de soporte
     */
    @GetMapping
    public ResponseEntity<?> getAllSupportUsers() {
        try {
            return ResponseEntity.ok(supportUserService.findAll());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al obtener los usuarios de soporte: " + ex.getMessage()));
        }
    }

    /**
     * Request DTO para login
     */
    public record LoginSupportRequest(
            @NotBlank
            @Email String email,
            @NotBlank String password
            ) {

    }

    /**
     * Request DTO para registro
     */
    public record RegisterSupportUserRequest(
            @NotBlank String name,
            @NotBlank String firstSurname,
            @NotBlank String secondSurname,
            @NotBlank
            @Email String email,
            @NotBlank
            @Size(min = 6) String password,
            boolean supervisor,
            @NotEmpty List<Integer> serviceIds
            ) {

    }

    /**
     * Request DTO para logout
     */
    public record LogoutRequest(Integer id, String role) {

    }
}
