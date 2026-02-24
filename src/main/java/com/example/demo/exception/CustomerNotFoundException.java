package com.example.demo.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Integer id) {
        super("Aucun client pour l'id : " + id);
    }
}
