package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date date;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customers customers;

    private Integer status;

    @ManyToOne
    @JoinColumn(name = "professional_id")
    private Professionals professionals;
}
