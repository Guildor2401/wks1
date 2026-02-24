package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.reposirtory.CustomerRepositoryy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.command.CreateCustomerCommand;
import com.example.demo.command.UptadeCustomerCommand;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.CustomerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepositoryy customerRepository;

    public List<Customer> GetAllCustomers() {
        return customerRepository.findAll();
    }

    public Map<String, String> getCustomerInfoById(Integer id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return null;
        }

        Map<String, String> info = new HashMap<>();
        info.put("firstname", customer.getFirstName());
        info.put("lastname", customer.getLastName());
        info.put("email", customer.getEmail());

        return info;
    }

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