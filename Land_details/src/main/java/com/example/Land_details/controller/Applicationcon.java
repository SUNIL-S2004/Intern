package com.example.Land_details.controller;

import com.example.Land_details.model.Applicationmodel;
import com.example.Land_details.model.Landmodel;
import com.example.Land_details.model.ApplicationStatus;
import com.example.Land_details.service.StatusService;
import com.example.Land_details.service.Landservice;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/application")
public class Applicationcon {

    @Autowired
    private StatusService applicationService;
    private Landservice landService;

    // Create new application
    @PostMapping
    public Applicationmodel creatapp(@Valid @RequestBody Applicationmodel app) {
        return applicationService.createApplication(app);
    }

    // Get status of application by ID
//    @GetMapping("/{id}/status")
//    public ResponseEntity<Status> getStatus(@PathVariable Long id) {
//        return applicationService.getApplicationById(id)
//                .map(app -> ResponseEntity.ok(app.getStatus()))
//                .orElse(ResponseEntity.notFound().build());
//    }
    
    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(app -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", app.getStatus().toString()); // Ensure status is a string
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Update only the status of an application
    @PutMapping("/{id}/status")
    public ResponseEntity<Applicationmodel> updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            String statusStr = body.get("status");
            ApplicationStatus status = ApplicationStatus.valueOf(statusStr.toUpperCase());
            Applicationmodel updated = applicationService.updateStatus(id, status);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update the full application object
    @PutMapping("/{id}")
    public ResponseEntity<Applicationmodel> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody Applicationmodel updatedApp) {

        Applicationmodel updated = applicationService.updateApplication(id, updatedApp);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Get all applications
    @GetMapping
    public List<Applicationmodel> getAllApplications() {
        return applicationService.getAllApplications();
    }

    // Get application with associated land details
    @GetMapping("/applications/{id}/details")
    public ResponseEntity<Map<String, Object>> getApplicationWithLand(@PathVariable Long id) {
        Optional<Applicationmodel> appOpt = applicationService.getApplicationById(id);
        if (appOpt.isEmpty()) return ResponseEntity.notFound().build();

        Applicationmodel app = appOpt.get();
        Landmodel land = app.getLandId(); 
        // ✅ now get Landmodel directly from Applicationmodel

        Map<String, Object> response = new HashMap<>();
        response.put("application", app);
        response.put("land", land);
        return ResponseEntity.ok(response);
    }
}
