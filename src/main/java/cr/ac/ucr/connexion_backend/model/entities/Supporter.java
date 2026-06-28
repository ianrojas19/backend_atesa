package cr.ac.ucr.connexion_backend.model.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "supporter")
public class Supporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "first_surname", nullable = false, length = 100)
    private String firstSurname;

    @Column(name = "second_surname", nullable = false, length = 100)
    private String secondSurname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Supervisor supervisor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "supporter_service",
        joinColumns = @JoinColumn(name = "supporter_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<ServiceType> services;

    public Supporter() {
    }

    public Supporter(String name, String firstSurname, String secondSurname, String email, String password, Supervisor supervisor, List<ServiceType> services) {
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.email = email;
        this.password = password;
        this.supervisor = supervisor;
        this.services = services;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstSurname() { return firstSurname; }
    public void setFirstSurname(String firstSurname) { this.firstSurname = firstSurname; }

    public String getSecondSurname() { return secondSurname; }
    public void setSecondSurname(String secondSurname) { this.secondSurname = secondSurname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Supervisor getSupervisor() { return supervisor; }
    public void setSupervisor(Supervisor supervisor) { this.supervisor = supervisor; }

    public List<ServiceType> getServices() { return services; }
    public void setServices(List<ServiceType> services) { this.services = services; }
}
