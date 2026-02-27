package com.example.demo.service;

import com.example.demo.command.CreateAppointmentCommand;
import com.example.demo.command.UpdateAppointmentCommand;
import com.example.demo.exception.AppointmentNotFoundException;
import com.example.demo.model.Appointments;
import com.example.demo.model.Customers;
import com.example.demo.model.Professionals;
import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProfessionalRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ProfessionalRepository professionalRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public AppointmentService(AppointmentRepository appointmentRepository,
                              CustomerRepository customerRepository,
                              ProfessionalRepository professionalRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.professionalRepository = professionalRepository;
    }

    public List<Appointments> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointments getAppointmentById(Integer id){
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    public List<Appointments> getAppointmentsByProfessional(Integer professionalId) {
        return appointmentRepository.findByProfessionalsId(professionalId);
    }

    public List<Appointments> getAppointmentsByCustomerId(Integer customerId) {
        return appointmentRepository.findByCustomersId(customerId);
    }

    public List<Appointments> getAppointmentsByDateRange(String startStr, String endStr) {
        Date start = parseDateOrNull(startStr);
        Date end = parseDateOrNull(endStr);
        if (start == null || end == null) return List.of();
        return appointmentRepository.findByDateBetween(start, end);
    }

    public void createAppointment(CreateAppointmentCommand command){
        Appointments appointment = new Appointments();

        appointment.setDuration(command.getDuration());
        appointment.setStatus(command.getStatus());

        Date parsedDate = parseDateOrNull(command.getDate());
        appointment.setDate(parsedDate);

        if (command.getCustomer_id() != null) {
            Customers customer = customerRepository.findById(command.getCustomer_id()).orElse(null);
            appointment.setCustomers(customer);
        }

        if (command.getProfessional_id() != null) {
            Professionals professional = professionalRepository.findById(command.getProfessional_id()).orElse(null);
            appointment.setProfessionals(professional);
        }

        appointmentRepository.save(appointment);
    }

    public void updateAppointment(UpdateAppointmentCommand command){
        Appointments appointment = appointmentRepository.findById(command.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(command.getId()));

        appointment.setDuration(command.getDuration());
        appointment.setStatus(command.getStatus());

        Date parsedDate = parseDateOrNull(command.getDate());
        appointment.setDate(parsedDate);

        if (command.getCustomer_id() != null) {
            Customers customer = customerRepository.findById(command.getCustomer_id()).orElse(null);
            appointment.setCustomers(customer);
        }

        if (command.getProfessional_id() != null) {
            Professionals professional = professionalRepository.findById(command.getProfessional_id()).orElse(null);
            appointment.setProfessionals(professional);
        }

        appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Integer id){
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException(id);
        }
        appointmentRepository.deleteById(id);
    }

    private Date parseDateOrNull(String dateStr){
        if (dateStr == null) return null;
        try{
            return dateFormat.parse(dateStr);
        } catch (ParseException e){
            return null;
        }
    }
}
