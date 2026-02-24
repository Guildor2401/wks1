package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.reposirtory.CustomerRepositoryy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepositoryy customerRepositoryy;

    public List<Customer> GetAllCustomers() {
        return customerRepositoryy.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepositoryy.findById(id).orElse(null);
    }
}
