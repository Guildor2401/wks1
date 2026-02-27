package com.example.demo.controller;

import com.example.demo.command.CreateAppointmentCommand;
import com.example.demo.command.UpdateAppointmentCommand;
import com.example.demo.model.Appointments;
import com.example.demo.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/AllAppointments")
    public List<Appointments> getAllAppointments(){
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointments getAppointment(@PathVariable Integer id){
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping("/ByProfessional/{professionalId}")
    public List<Appointments> getByProfessional(@PathVariable Integer professionalId) {
        return appointmentService.getAppointmentsByProfessional(professionalId);
    }

    @GetMapping("/ByCustomer/{customerId}")
    public List<Appointments> getByCustomer(@PathVariable Integer customerId) {
        return appointmentService.getAppointmentsByCustomerId(customerId);
    }

    @GetMapping("/ByDateRange")
    public List<Appointments> getByDateRange(@RequestParam String start, @RequestParam String end) {
        return appointmentService.getAppointmentsByDateRange(start, end);
    }

    @PostMapping("/CreateAppointment")
    public void createAppointment(@RequestBody CreateAppointmentCommand command){
        appointmentService.createAppointment(command);
    }

    @PutMapping("/UpdateAppointment")
    public void updateAppointment(@RequestBody UpdateAppointmentCommand command){
        appointmentService.updateAppointment(command);
    }

    @DeleteMapping("/DeleteAppointment/{id}")
    public void deleteAppointment(@PathVariable Integer id){
        appointmentService.deleteAppointment(id);
    }
}
