package com.example.demo.repository;

import com.example.demo.model.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments,Integer> {
    List<Appointments> findByProfessionalsId(Integer professionalId);
    List<Appointments> findByCustomersId(Integer customerId);
    List<Appointments> findByDateBetween(Date start, Date end);
    List<Appointments> findByDateAndProfessionalsId(Date date, Integer professionalId);
    List<Appointments> findByDateAndCustomersId(Date date, Integer customerId);
}
