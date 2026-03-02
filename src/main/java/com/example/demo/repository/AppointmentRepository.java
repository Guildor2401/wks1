package com.example.demo.repository;

import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments,Integer> {
    List<Appointments> findByProfessionalsId(Integer professionalId);
    List<Appointments> findByCustomersId(Integer customerId);
    List<Appointments> findByDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, AppointmentStatus status);
    List<Appointments> findByDateAndProfessionalsId(LocalDateTime date, Integer professionalId);
    List<Appointments> findByDateAndCustomersId(LocalDateTime date, Integer customerId);
    List<Appointments> findByStatus(AppointmentStatus status);
}
