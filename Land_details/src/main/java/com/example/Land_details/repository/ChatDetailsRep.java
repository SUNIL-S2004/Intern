package com.example.Land_details.repository;

import com.example.Land_details.model.ChatDetailsmod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatDetailsRep extends JpaRepository<ChatDetailsmod, Long> {

    // Fetch unique districts
    List<ChatDetailsmod> findByDistrictIsNotNull();

    // Fetch taluks by district name
    List<ChatDetailsmod> findByDistrict(String district);

    // Fetch villages by taluk name
    List<ChatDetailsmod> findByTaluk(String taluk);

    // Fetch full record by district + taluk + village (for code lookup)
    List<ChatDetailsmod> findByDistrictAndTalukAndVillage(String district, String taluk, String village);
}
