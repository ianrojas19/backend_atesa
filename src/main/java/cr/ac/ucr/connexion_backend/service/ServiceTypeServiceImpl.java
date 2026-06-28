package cr.ac.ucr.connexion_backend.service;

import cr.ac.ucr.connexion_backend.model.entities.ServiceType;
import cr.ac.ucr.connexion_backend.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Override
    public List<ServiceType> getAllServices() {
        return serviceTypeRepository.findAll();
    }
}
