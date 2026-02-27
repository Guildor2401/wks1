package com.example.demo.controller;

import com.example.demo.command.CreateProfessionalCommand;
import com.example.demo.command.UpdateProfessionalCommand;
import com.example.demo.model.Professionals;
import com.example.demo.service.ProfessionalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/professional")
public class ProfessionalController {
    ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping("/AllProfessionals")
    public List<Professionals> GetAllProfessionals()
    {
        return professionalService.getAllProfessionals();
    }

    @GetMapping("/GetOneProfessional/{id}")
    public Map<String, String> GetOneProfessional(@PathVariable Integer id)
    {
        return professionalService.getProfessionalInfoById(id);
    }

    @PostMapping("/CreateProfessional")
    public void CreateProfessional(@RequestBody CreateProfessionalCommand command) {
        professionalService.createProfessional(command);
    }

    @PutMapping("/UpdateProfessional")
    public void UpdateProfessional(@RequestBody UpdateProfessionalCommand command) {
        professionalService.updateProfessional(command);
    }

    @DeleteMapping("/DeleteProfessional")
    public void DeleteProfessional(@RequestParam Integer id) {
        professionalService.deleteProfessionalById(id);
    }
}
