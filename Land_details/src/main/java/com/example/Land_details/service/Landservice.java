package com.example.Land_details.service;

import com.example.Land_details.model.*;
import com.example.Land_details.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Landservice{
	@Autowired
	private Landrepository landRepository;
	
	public List<Landmodel> getAllLands(){
		return landRepository.findAll();
	}
	
	public Optional<Landmodel> getLandById(Long id) {
	    return landRepository.findById(id);
	}

	public Landmodel creatland(Landmodel land) {
		return landRepository.save(land);
	}
	
	public void deleteLand(Long id) {
		landRepository.deleteById(id);
	}
}