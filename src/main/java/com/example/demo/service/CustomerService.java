package com.example.demo.service;

import com.example.demo.command.CreateCustomerCommand;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;

public class CustomerService {
    private CustomerRepository customerRepository;

    public void createCustomer(CreateCustomerCommand command) {

        Customer customerToCreate = Customer.builder()
                .id(command.getId())
                .lastName( command.getLastName())
                .firstName(command.getFirstName())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .build();

        customerRepository.save(customerToCreate);
    }
}
