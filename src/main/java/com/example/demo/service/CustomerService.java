package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.reposirtory.CustomerRepositoryy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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


}
