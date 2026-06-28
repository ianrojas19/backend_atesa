package cr.ac.ucr.connexion_backend.service;

import cr.ac.ucr.connexion_backend.model.entities.ServiceType;
import java.util.List;

public interface ServiceTypeService {
    List<ServiceType> getAllServices();
}