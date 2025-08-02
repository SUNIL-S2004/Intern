package com.example.Land_details.controller;

import com.example.Land_details.model.Landmodel;
import com.example.Land_details.service.Landservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/lands")
public class LandController {
	@Autowired
	private Landservice landService;
	
	@GetMapping
	public List<Landmodel> getAllLands(){
		return landService.getAllLands();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Landmodel> getLandById(@PathVariable Long id){
		return landService.getLandById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping()
	public Landmodel createLand(@RequestBody Landmodel land) {
		return landService.creatland(land);
	}
	
	@PutMapping("/{id}")
	public Landmodel updateLand(@PathVariable Long id, @RequestBody Landmodel landDetails) {
	    Optional<Landmodel> existingLand = landService.getLandById(id);
	    if (existingLand.isPresent()) {
	        Landmodel land = existingLand.get();
	        land.setArea(landDetails.getArea());
	        land.setLocation(landDetails.getLocation());
	        return landService.creatland(land);
	    } else {
	        throw new RuntimeException("Land not found with id: " + id);
	    }
	}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLand(@PathVariable Long id) {
        landService.deleteLand(id);
        return ResponseEntity.noContent().build();
    }
}
