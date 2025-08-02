package com.example.Land_details.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Land_details.model.ApplicationDetails;
import com.example.Land_details.model.ChatDetailsmod;
import com.example.Land_details.repository.ApplicationDetailsRepo;
import com.example.Land_details.repository.ChatDetailsRep;
import com.example.Land_details.model.ApplicationStatus;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ChatDetails {

    @Autowired
    private ChatDetailsRep repository;
    @Autowired
    private ApplicationDetailsRepo applicationRepo;

    private int serialCounter = 1; // simple in-memory counter (not recommended for production)

    @GetMapping("/districts")
    public List<String> getDistricts() {
        return repository.findByDistrictIsNotNull().stream()
                .map(ChatDetailsmod::getDistrict)
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/taluks")
    public List<String> getTaluks(@RequestParam String district) {
        return repository.findByDistrict(district).stream()
                .map(ChatDetailsmod::getTaluk)
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/village")
    public List<String> getVillages(@RequestParam String taluk) {
        return repository.findByTaluk(taluk).stream()
                .map(ChatDetailsmod::getVillage)
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/survey")
    public List<Integer> getSurvey_no() {
        return List.of(1, 2, 3, 4, 5);
    }

    @GetMapping("/subdivision")
    public List<Integer> getSubdivision_no(@RequestParam Integer Survey) {
        return switch (Survey) {
            case 1 -> List.of(1, 2);
            case 2 -> List.of(1, 4);
            case 3 -> List.of(3, 2);
            case 4 -> List.of(4, 1);
            default -> List.of(1);
        };
    }

     @GetMapping("/land-type")
    public List<String> getLandType() {
        return List.of("Not Involving Subdivision", "Involving Subdivision", "Measurement of field boundary", "F-LINE Appeal");
    }
    
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveApplication(@RequestBody Map<String, String> data) {
        String district = data.get("district");
        String taluk = data.get("taluk");
        String village = data.get("village");
        int survey = Integer.parseInt(data.get("survey"));
        int subdivision = Integer.parseInt(data.get("subdivision"));

        // Fetch codes from existing table
        List<ChatDetailsmod> matched = repository.findByDistrictAndTalukAndVillage(district, taluk, village);
        if (matched.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid location data"));
        }

        ChatDetailsmod record = matched.get(0);
        String districtCode = record.getDistrictCode();
        String talukCode = record.getTalukCode();
        String villageCode = record.getVillageCode();
        String year = String.valueOf(Year.now().getValue());
        Integer lastSerial = applicationRepo.findMaxSerial(); // You’ll write this method next
        int nextSerial = (lastSerial == null) ? 1 : lastSerial + 1;

        String serial = String.format("%02d", nextSerial);

        String applicationId = String.join("-",
                year,
                districtCode,
                talukCode,
                villageCode,
                String.valueOf(survey),
                String.valueOf(subdivision),
                serial
        );

        // Save into application_details table
        ApplicationDetails app = new ApplicationDetails();
        app.setApplicationId(applicationId);
        app.setDistrict(district);
        app.setDistrictCode(districtCode);
        app.setTaluk(taluk);
        app.setTalukCode(talukCode);
        app.setVillage(village);
        app.setVillageCode(villageCode);
        app.setSurveyNo(survey);
        app.setSubdivisionNo(subdivision);
        app.setApplicationStatus(ApplicationStatus.inprocess);


        applicationRepo.save(app); // <--- Save to DB

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Application submitted successfully!");
        response.put("applicationId", applicationId);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/status")
    public ResponseEntity<?> checkStatus(@RequestParam String applicationId) {
        Optional<ApplicationDetails> optional = applicationRepo.findByApplicationId(applicationId);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Application ID not found"));
        }

        ApplicationDetails app = optional.get();
        return ResponseEntity.ok(Map.of(
            "applicationId", app.getApplicationId(),
            "status", app.getApplicationStatus()
        ));
    }

    @PutMapping("/status-update/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam ApplicationStatus status) {
        Optional<ApplicationDetails> optional = applicationRepo.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ApplicationDetails app = optional.get();
        app.setApplicationStatus(status);
        applicationRepo.save(app);

        return ResponseEntity.ok(Map.of("message", "Status updated", "status", status));
    }


}
