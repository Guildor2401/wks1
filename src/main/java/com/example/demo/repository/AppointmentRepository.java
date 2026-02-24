package com.example.demo.repository;

import com.example.demo.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointments,Integer> {
}
