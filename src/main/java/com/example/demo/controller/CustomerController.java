package com.example.demo.controller;

import com.example.demo.command.CreateCustomerCommand;
import com.example.demo.model.Customers;
import com.example.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/customer")
public class CustomerController {
    CustomerService customerService;

    @GetMapping("/AllCustomers")
    public List<Customers> GetAllCustomers()
    {
        return customerService.GetAllCustomers();
    }

    @GetMapping("/GetOneCustomer/{id}")
    public Map<String, String> GetOneCustomer(@PathVariable Integer id)
    {
        return customerService.getCustomerInfoById(id);
    }

    @PostMapping("/CreateCustomer")
    public void CreateCustomer(@RequestBody CreateCustomerCommand command) {
        customerService.createCustomer(command);
    }

    @PutMapping("/UpdateCustomer")
    public void UpdateCustomer(@RequestBody CreateCustomerCommand command) {
        customerService.createCustomer(command);
    }
}
