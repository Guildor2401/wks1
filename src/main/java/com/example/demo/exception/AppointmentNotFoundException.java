package com.example.demo.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Integer id) {
        super("Aucun rendez-vous pour l'id : " + id);
    }
}

