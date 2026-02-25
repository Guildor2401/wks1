package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Professionals;

public interface ProfessionalRepository extends JpaRepository<Professionals,Integer> {

}
