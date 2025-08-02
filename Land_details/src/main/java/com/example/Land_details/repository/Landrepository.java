package com.example.Land_details.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Land_details.model.Landmodel;

@Repository
public interface Landrepository extends JpaRepository<Landmodel,Long>{
	
}