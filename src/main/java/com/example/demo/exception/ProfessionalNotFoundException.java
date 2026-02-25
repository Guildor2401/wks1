package com.example.demo.exception;

public class ProfessionalNotFoundException extends RuntimeException {
    public ProfessionalNotFoundException(Integer id) {
        super("Aucun professionnel pour l'id : " + id);
    }
}

