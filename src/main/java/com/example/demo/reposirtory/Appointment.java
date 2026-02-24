package com.example.demo.reposirtory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Appointment extends JpaRepository<Appointment,String> {
}
