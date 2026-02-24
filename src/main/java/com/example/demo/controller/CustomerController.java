package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.reposirtory.CustomerRepositoryy;
import com.example.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/customer")
public class CustomerController {
    CustomerService customerService;

    @GetMapping("/AllCustomers")
    public List<Customer> GetAllCustomers()
    {
        return customerService.GetAllCustomers();
    }

    @GetMapping("/GetOneCustomer")
    public Customer GetOneCustomer(Integer id)
    {
        return customerService.getCustomerById(id);
    }
}
