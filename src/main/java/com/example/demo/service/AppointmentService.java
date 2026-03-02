package com.example.demo.service;

import com.example.demo.command.CreateAppointmentCommand;
import com.example.demo.command.UpdateAppointmentCommand;
import com.example.demo.exception.AppointmentNotFoundException;
import com.example.demo.model.AppointmentStatus;
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
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        LocalDateTime start = parseDateOrNull(startStr);
        LocalDateTime end = parseDateOrNull(endStr);
        if (start == null || end == null) return List.of();
        return appointmentRepository.findByDateBetweenAndStatus(start, end, AppointmentStatus.planned);
    }

    public void createAppointment(CreateAppointmentCommand command) {

        if (command.getDate() == null || command.getDuration() == null) {
            throw new IllegalArgumentException("La date et la durée sont obligatoires.");
        }

        LocalDateTime parsedDate = parseDateOrNull(command.getDate());
        if (parsedDate == null) {
            throw new IllegalArgumentException("Format de date invalide.");
        }

        Professionals professional = null;
        if (command.getProfessional_id() != null) {
            professional = professionalRepository.findById(command.getProfessional_id())
                    .orElseThrow(() -> new RuntimeException("Professionnel introuvable."));
        }

        Customers customer = null;
        if (command.getCustomer_id() != null) {
            customer = customerRepository.findById(command.getCustomer_id())
                    .orElseThrow(() -> new RuntimeException("Client introuvable."));
        }

        LocalDateTime newStart = parsedDate;

        LocalDateTime newEnd = newStart.plusMinutes(command.getDuration());

        if (professional != null) {

            List<Appointments> professionalAppointments =
                    appointmentRepository.findByProfessionalsId(professional.getId());

            for (Appointments existing : professionalAppointments) {

                LocalDateTime existingStart = existing.getDate();

                LocalDateTime existingEnd =
                        existingStart.plusMinutes(existing.getDuration());

                if (overlaps(existingStart, existingEnd, newStart, newEnd)) {
                    throw new RuntimeException("Le professionnel est déjà occupé à ce créneau.");
                }
            }
        }

        if (customer != null) {

            List<Appointments> customerAppointments =
                    appointmentRepository.findByCustomersId(customer.getId());

            for (Appointments existing : customerAppointments) {

                LocalDateTime existingStart = existing.getDate();

                LocalDateTime existingEnd =
                        existingStart.plusMinutes(existing.getDuration());

                if (overlaps(existingStart, existingEnd, newStart, newEnd)) {
                    throw new RuntimeException("Le client a déjà un rendez-vous à ce créneau.");
                }
            }
        }

        Appointments appointment = new Appointments();
        appointment.setDate(parsedDate);
        appointment.setDuration(command.getDuration());
        appointment.setStatus(command.getStatus());
        appointment.setProfessionals(professional);
        appointment.setCustomers(customer);

        appointmentRepository.save(appointment);
    }

    public List<Appointments> getAppointmentsByDateAndProfessional (String dateStr, Integer professionalId) {
        LocalDateTime date = parseDateOrNull(dateStr);
        if (date == null) return List.of();
        return appointmentRepository.findByDateAndProfessionalsId(date, professionalId);
    }

    public List<Appointments> getAppointmentsByDateAndCustomer (String dateStr, Integer customerId) {
        LocalDateTime date = parseDateOrNull(dateStr);
        if (date == null) return List.of();
        return appointmentRepository.findByDateAndCustomersId(date, customerId);
    }

    public void updateAppointment(UpdateAppointmentCommand command){
        Appointments appointment = appointmentRepository.findById(command.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(command.getId()));

        appointment.setDuration(command.getDuration());
        appointment.setStatus(command.getStatus());

        LocalDateTime parsedDate = parseDateOrNull(command.getDate());
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

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime parseDateOrNull(String dateStr) {
        if (dateStr == null) return null;

        try {
            return LocalDateTime.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
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


    private boolean hasConflict(Integer professionalId, Date newStart, int duration) {

        LocalDateTime newStartLdt = newStart.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        LocalDateTime newEndLdt = newStartLdt.plusMinutes(duration);

        List<Appointments> existingAppointments =
                appointmentRepository.findByProfessionalsId(professionalId);

        for (Appointments a : existingAppointments) {

            LocalDateTime existingStart = a.getDate();

            LocalDateTime existingEnd =
                    existingStart.plusMinutes(a.getDuration());

            if (overlaps(existingStart, existingEnd, newStartLdt, newEndLdt)) {
                return true;
            }
        }
        return false;
    }

    public void canceledAppointment(Integer id)
    {
        Appointments appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointment.setStatus(AppointmentStatus.cancel);
        appointmentRepository.save(appointment);
    }

    public void finishedAppointment(Integer id)
    {
        Appointments appointment =  appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));

        appointment.setStatus(AppointmentStatus.finish);
        appointmentRepository.save(appointment);
    }
}
