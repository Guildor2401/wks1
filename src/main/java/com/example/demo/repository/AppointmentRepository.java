package com.example.demo.repository;

import com.example.demo.model.AppointmentStatus;
import com.example.demo.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments,Integer> {
    List<Appointments> findByProfessionalsId(Integer professionalId);
    List<Appointments> findByCustomersId(Integer customerId);
    List<Appointments> findByDateBetweenAndStatus(Date start, Date end, AppointmentStatus status);
    List<Appointments> findByDateAndProfessionalsId(Date date, Integer professionalId);
    List<Appointments> findByDateAndCustomersId(Date date, Integer customerId);
    List<Appointments> findByStatus(AppointmentStatus status);
}
