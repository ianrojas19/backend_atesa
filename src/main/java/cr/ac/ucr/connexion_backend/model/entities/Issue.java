/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.connexion_backend.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author sebas
 */
@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "register_timestamp")
    private LocalDateTime registerTimestamp;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private IssueStatus status;

    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "client_id")
    private Integer clientId;
//agregue esto luis
    @Column(name = "supporter_id")
    private Integer supporterId;

    public Issue() {
    }

    public Issue(String description, LocalDateTime registerTimestamp,
            String address, String contactPhone, String contactEmail,
            IssueStatus status, Integer serviceId, Integer clientId) {
        this.description = description;
        this.registerTimestamp = registerTimestamp;
        this.address = address;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.status = status;
        this.serviceId = serviceId;
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getRegisterTimestamp() {
        return registerTimestamp;
    }

    public void setRegisterTimestamp(LocalDateTime registerTimestamp) {
        this.registerTimestamp = registerTimestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
//agregue esto luis

    public Integer getSupporterId() {
        return supporterId;
    }

    public void setSupporterId(Integer supporterId) {
        this.supporterId = supporterId;
    }
}