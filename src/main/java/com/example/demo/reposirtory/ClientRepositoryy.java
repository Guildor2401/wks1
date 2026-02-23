package com.example.demo.reposirtory;

import com.example.demo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepositoryy extends JpaRepository<Customer,String> {
}
