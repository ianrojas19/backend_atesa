package cr.ac.ucr.connexion_backend.service;

import java.util.List;
import java.util.Optional;
import cr.ac.ucr.connexion_backend.model.entities.Supervisor;
import cr.ac.ucr.connexion_backend.model.entities.Supporter;
import cr.ac.ucr.connexion_backend.model.entities.ServiceType;
import cr.ac.ucr.connexion_backend.repository.SupervisorRepository;
import cr.ac.ucr.connexion_backend.repository.SupporterRepository;
import cr.ac.ucr.connexion_backend.repository.ServiceTypeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupportUserServiceImpl implements SupportUserService {

    private final SupervisorRepository supervisorRepository;
    private final SupporterRepository supporterRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public SupportUserServiceImpl(
            SupervisorRepository supervisorRepository,
            SupporterRepository supporterRepository,
            ServiceTypeRepository serviceTypeRepository) {
        this.supervisorRepository = supervisorRepository;
        this.supporterRepository = supporterRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Override
    public Optional<AuthenticatedSupportUser> login(String email, String password) {
        Optional<Supervisor> supervisor = supervisorRepository.findByEmail(email);
        if (supervisor.isPresent() && supervisor.get().getPassword().equals(password)) {
            Supervisor s = supervisor.get();
            String fullName = s.getName() + " " + s.getFirstSurname() + " " + s.getSecondSurname();
            return Optional.of(new AuthenticatedSupportUser(s.getId(), s.getEmail(), fullName, "supervisor"));
        }

        Optional<Supporter> supporter = supporterRepository.findByEmail(email);
        if (supporter.isPresent() && supporter.get().getPassword().equals(password)) {
            Supporter s = supporter.get();
            String fullName = s.getName() + " " + s.getFirstSurname() + " " + s.getSecondSurname();
            return Optional.of(new AuthenticatedSupportUser(s.getId(), s.getEmail(), fullName, "supporter"));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean register(
            String name,
            String firstSurname,
            String secondSurname,
            String email,
            String password,
            boolean supervisor,
            List<Integer> serviceIds) {

        if (supervisorRepository.existsByEmail(email) || supporterRepository.existsByEmail(email)) {
            return false;
        }

        List<ServiceType> selectedServices = null;
        if (!supervisor) {
            selectedServices = serviceTypeRepository.findAllById(serviceIds);
            if (selectedServices.size() != serviceIds.size()) {
                throw new IllegalArgumentException("Algunos servicios seleccionados no son válidos.");
            }
        }

        try {
            if (supervisor) {
                Supervisor newSupervisor = new Supervisor(name, firstSurname, secondSurname, email, password);
                supervisorRepository.save(newSupervisor);
            } else {
                Supporter newSupporter = new Supporter(name, firstSurname, secondSurname, email, password, null, selectedServices);
                supporterRepository.save(newSupporter);
            }
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }

    @Override
    public List<Supporter> findAll() {
        return supporterRepository.findAll();
    }
}
