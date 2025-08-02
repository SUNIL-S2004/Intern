package com.example.Land_details.model;

import jakarta.persistence.*;

@Entity
@Table(name = "district_taluk_village")
public class ChatDetailsmod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "district_code")
    private String districtCode;

    private String district;

    @Column(name = "taluk_code")
    private String talukCode;

    private String taluk;

    @Column(name = "village_code")
    private String villageCode;

    private String village;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTalukCode() {
        return talukCode;
    }

    public void setTalukCode(String talukCode) {
        this.talukCode = talukCode;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
}
