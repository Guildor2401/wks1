package com.example.demo.command;

import lombok.Getter;

@Getter
public class CreateCustomerCommand {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
