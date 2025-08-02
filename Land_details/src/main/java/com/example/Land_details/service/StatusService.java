package com.example.Land_details.service;

import com.example.Land_details.model.Applicationmodel;
import com.example.Land_details.model.ApplicationStatus;
//import com.example.Land_details.model.Landmodel;
import com.example.Land_details.repository.Applicationstatu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatusService {

    @Autowired
    private Applicationstatu applicationRepository;

    public Applicationmodel createApplication(Applicationmodel app) {
        app.setStatus(ApplicationStatus.INPROCESS);
        return applicationRepository.save(app);
    }

    public Optional<Applicationmodel> getApplicationById(Long id){
        return applicationRepository.findById(id);
    }

    public Applicationmodel updateStatus(Long id, ApplicationStatus newStatus) {
        return applicationRepository.findById(id).map(app -> {
            app.setStatus(newStatus);
            return applicationRepository.save(app);
        }).orElse(null);
    }

    public Applicationmodel updateApplication(Long id, Applicationmodel updatedApp) {
        return applicationRepository.findById(id).map(app -> {
            app.setLandId(updatedApp.getLandId());
            app.setApplicantName(updatedApp.getApplicantName());
            return applicationRepository.save(app);
        }).orElse(null);
    }

    public List<Applicationmodel> getAllApplications() {
        return applicationRepository.findAll();
    }
}

