package com.example.Land_details.repository;

import com.example.Land_details.model.Applicationmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface Applicationstatu extends JpaRepository<Applicationmodel,Long>{
	List<Applicationmodel> findByLandId_Id(Long landId);

}