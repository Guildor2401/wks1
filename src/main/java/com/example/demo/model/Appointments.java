package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "appointment_date")
    private LocalDateTime date;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customers customers;

    @Column(name = "status", nullable = false,
            columnDefinition = "appointment_status")   // indique le type PostgreSQL
    @Enumerated(EnumType.STRING)                     // stocke la valeur texte (PENDING, …)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Professionals professionals;
}


