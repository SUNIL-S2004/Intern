package com.example.Land_details.model;
import com.example.Land_details.model.ApplicationStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "application_details")
public class ApplicationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicationId;

    private String district;
    private String districtCode;

    private String taluk;
    private String talukCode;

    private String village;
    private String villageCode;

    private int surveyNo;
    private int subdivisionNo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus applicationStatus = ApplicationStatus.inprocess;

    // getters and setters
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

	private String landType;

	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getTaluk() {
		return taluk;
	}
	public void setTaluk(String taluk) {
		this.taluk = taluk;
	}
	public String getTalukCode() {
		return talukCode;
	}
	public void setTalukCode(String talukCode) {
		this.talukCode = talukCode;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getVillageCode() {
		return villageCode;
	}
	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}
	public int getSurveyNo() {
		return surveyNo;
	}
	public void setSurveyNo(int surveyNo) {
		this.surveyNo = surveyNo;
	}
	public int getSubdivisionNo() {
		return subdivisionNo;
	}
	public void setSubdivisionNo(int subdivisionNo) {
		this.subdivisionNo = subdivisionNo;
	}

	public String getLandType() {
		return landType;
	}
	public void setLandType(String landType) {
		this.landType = landType;
	}
	

    // Getters and Setters
    // (You can use Lombok to avoid boilerplate)
    // ...
}
