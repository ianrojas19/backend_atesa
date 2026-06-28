package cr.ac.ucr.connexion_backend.controller;

import cr.ac.ucr.connexion_backend.model.entities.ServiceType;
import cr.ac.ucr.connexion_backend.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceTypeController {

    @Autowired
    private ServiceTypeService serviceTypeService;

    @GetMapping
    public List<ServiceType> getServicesCatalog() {
        return serviceTypeService.getAllServices();
    }
}