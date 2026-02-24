package com.example.demo.service;

import com.example.demo.model.Customers;
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

    private final CustomerRepository customerRepository;

    public List<Customers> GetAllCustomers() {
        return customerRepository.findAll();
    }

    public Map<String, String> getCustomerInfoById(Integer id) {
        Customers customers = customerRepository.findById(id).orElse(null);
        if (customers == null) {
            return null;
        }

        Map<String, String> info = new HashMap<>();
        info.put("firstname", customers.getFirst_name());
        info.put("lastname", customers.getLast_name());
        info.put("email", customers.getEmail());

        return info;
    }

    public void createCustomer(CreateCustomerCommand command) {

        Customers customerToCreate = Customers.builder()
                .last_name( command.getLast_name())
                .first_name(command.getFirst_name())
                .email(command.getEmail())
                .phone_number(command.getPhone_number())
                .build();

        customerRepository.save(customerToCreate);
    }

    public void updateCustomer(UptadeCustomerCommand command){
        Customers customerToUptade = findCustomerByIdOrThrow(command.getId());

        customerToUptade.setFirst_name(command.getFirst_name());
        customerToUptade.setLast_name(command.getLast_name());
        customerToUptade.setEmail(command.getEmail());
        customerToUptade.setPhone_number(command.getPhone_number());

        customerRepository.save(customerToUptade);
    }

    private Customers findCustomerByIdOrThrow(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
}
}