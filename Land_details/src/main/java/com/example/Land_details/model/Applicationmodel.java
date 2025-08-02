package com.example.Land_details.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name="application")
public class Applicationmodel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long applicationid;
	@ManyToOne
	@JoinColumn(name = "land_id")
	private Landmodel landId;
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	@NotBlank(message = "Applicant name is required")
	private String applicantName;
	
	public Long getApplicationid() {
	    return applicationid;
	}
	public void setApplicationid(Long applicationid) {
	    this.applicationid = applicationid;
	}

	public Landmodel getLandId() {
	    return landId;
	}
	public void setLandId(Landmodel landId) {
	    this.landId = landId;
	}

	public ApplicationStatus getStatus() {
	    return status;
	}
	public void setStatus(ApplicationStatus status) {
	    this.status = status;
	}

	public String getApplicantName() {
	        return applicantName;
	 }

	public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
	}
//	@ManyToOne
//	@JoinColumn(name = "land_id", referencedColumnName = "id")
//	private Landmodel land;
//
//	public Landmodel getLand() {
//	    return land;
//	}
//
//	public void setLand(Landmodel land) {
//	    this.land = land;
//	}

}
