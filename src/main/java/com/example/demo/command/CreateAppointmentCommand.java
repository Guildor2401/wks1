package com.example.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAppointmentCommand {
    @JsonProperty("date")
    private String date; // ISO string -> will parse to Date in service

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("customer_id")
    private Integer customer_id;

    @JsonProperty("professional_id")
    private Integer professional_id;

    @JsonProperty("status")
    private Integer status;

    public CreateAppointmentCommand() {}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getProfessional_id() {
        return professional_id;
    }

    public void setProfessional_id(Integer professional_id) {
        this.professional_id = professional_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

