package com.example.demo.service;

import com.example.demo.command.CreateCustomerCommand;
import com.example.demo.command.UptadeCustomerCommand;
import com.example.demo.exception.CustomerNotFoundException;
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

    public void updateCustomer(UptadeCustomerCommand command){
        Customer customerToUptade = findCustomerByIdOrThrow(command.getId());

        customerToUptade.setFirstName(command.getFirstName());
        customerToUptade.setLastName(command.getLastName());
        customerToUptade.setEmail(command.getEmail());
        customerToUptade.setPhoneNumber(command.getPhoneNumber());

        customerRepository.save(customerToUptade);
    }

    private Customer findCustomerByIdOrThrow(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
