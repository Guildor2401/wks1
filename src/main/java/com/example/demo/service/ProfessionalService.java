package com.example.demo.service;

import com.example.demo.model.Professionals;
import org.springframework.stereotype.Service;
import com.example.demo.command.CreateProfessionalCommand;
import com.example.demo.command.UpdateProfessionalCommand;
import com.example.demo.repository.ProfessionalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    public List<Professionals> getAllProfessionals() {
        return professionalRepository.findAll();
    }

    public Map<String, String> getProfessionalInfoById(Integer id) {
        Professionals professional = professionalRepository.findById(id).orElse(null);
        if (professional == null) {
            return null;
        }

        Map<String, String> info = new HashMap<>();
        info.put("name", professional.getName());
        info.put("profession", professional.getProfession());
        info.put("email", professional.getEmail());

        return info;
    }

    public void createProfessional(CreateProfessionalCommand command) {

        Professionals professionalToCreate = Professionals.builder()
                .name(command.getName())
                .profession(command.getProfession())
                .email(command.getEmail())
                .phoneNumber(command.getPhone_number())
                .build();

        professionalRepository.save(professionalToCreate);
    }

    public void updateProfessional(UpdateProfessionalCommand command){
        Professionals professionalToUpdate = findProfessionalByIdOrThrow(command.getId());

        professionalToUpdate.setName(command.getName());
        professionalToUpdate.setProfession(command.getProfession());
        professionalToUpdate.setEmail(command.getEmail());
        professionalToUpdate.setPhoneNumber(command.getPhone_number());

        professionalRepository.save(professionalToUpdate);
    }

    private Professionals findProfessionalByIdOrThrow(Integer id) {
        return professionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aucun professionnel pour l'id : " + id));
    }
}