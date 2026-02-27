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
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        List<Appointments> possibleConflicts = appointmentRepository.findByDateBetween(appointment.getDate(), computeEndTime(appointment.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime(), appointment.getDuration()));

        System.out.println("possibleConflicts = " + possibleConflicts);

        if (possibleConflicts.stream().anyMatch(a -> a.getProfessionals() != null && a.getProfessionals().getId().equals(command.getProfessional_id()))) {
            throw new RuntimeException("Le professionnel est déjà occupé à ce créneau.");
        }

        if (possibleConflicts.stream().anyMatch(a -> a.getCustomers() != null && a.getCustomers().getId().equals(command.getCustomer_id()))) {
            throw new RuntimeException("Le client a déjà un rendez-vous à ce créneau.");
        }


        appointmentRepository.save(appointment);
    }

    public List<Appointments> getAppointmentsByDateAndProfessional (String dateStr, Integer professionalId) {
        Date date = parseDateOrNull(dateStr);
        if (date == null) return List.of();
        return appointmentRepository.findByDateAndProfessionalsId(date, professionalId);
    }

    public List<Appointments> getAppointmentsByDateAndCustomer (String dateStr, Integer customerId) {
        Date date = parseDateOrNull(dateStr);
        if (date == null) return List.of();
        return appointmentRepository.findByDateAndCustomersId(date, customerId);
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
    /**
     * Retourne l’heure de fin d’un rendez‑vous.
     *
     * @param start   la date/heure de début (ex. 2026‑02‑27T18:00)
     * @param minutes durée en minutes
     * @return date/heure de fin
     */
    public static Date computeEndTime(LocalDateTime start, int minutes) {
        return Date.from(start.plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Vérifie si un créneau donné (de start à end) intersecte la plage du rendez‑vous.
     *
     * @param appointmentStart  début du rendez‑vous
     * @param appointmentEnd    fin du rendez‑vous (déjà calculée)
     * @param slotStart         début du créneau à tester
     * @param slotEnd           fin du créneau à tester
     * @return true si les deux intervalles se chevauchent, false sinon
     */
    public static boolean overlaps(LocalDateTime appointmentStart,
                                   LocalDateTime appointmentEnd,
                                   LocalDateTime slotStart,
                                   LocalDateTime slotEnd) {
        // Deux intervalles se chevauchent si le début de l’un est avant la fin de l’autre
        // ET la fin de l’un est après le début de l’autre.
        return !appointmentEnd.isBefore(slotStart) && !appointmentStart.isAfter(slotEnd);
    }
}
