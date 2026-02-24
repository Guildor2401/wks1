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
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date date;
    private Integer hours;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Customer customer;
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "professionels_id")
    private Professional professional;
}
