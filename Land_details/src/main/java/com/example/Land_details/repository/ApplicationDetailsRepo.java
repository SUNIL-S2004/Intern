package com.example.Land_details.repository;

import com.example.Land_details.model.ApplicationDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDetailsRepo extends JpaRepository<ApplicationDetails, Long> {
	Optional<ApplicationDetails> findByApplicationId(String applicationId);
	@Query("SELECT MAX(CAST(SUBSTRING(a.applicationId, LENGTH(a.applicationId) - 1) AS int)) FROM ApplicationDetails a")
	Integer findMaxSerial();

}
