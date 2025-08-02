package com.example.Land_details.model;

import jakarta.persistence.*;
@Entity
@Table(name="land_details")
public class Landmodel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	private String owner_name;
	private String location;
	private double area;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id =id;
	}
	
//	public String getowner_name() {
//		return owner_name;
//	}
//	public void setowner_name(String owner_name) {
//		this.owner_name=owner_name;
//	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location=location;
	}
	
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area=area;
	}
	
}

