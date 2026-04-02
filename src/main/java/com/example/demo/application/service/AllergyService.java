package com.example.demo.application.service;

import com.example.demo.entity.Allergy;
import com.example.demo.entity.User;
import com.example.demo.model.AllergyDTO;
import com.example.demo.repository.AllergyRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AllergyService {

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private UserRepository userRepository;

    // 🔹 GET allergy por ID
    public Optional<AllergyDTO> getAllergyById(Integer id) {
        return allergyRepository.findById(id)
                .map(this::toDTO);
    }

    // 🔹 CREATE allergy
    public AllergyDTO saveAllergy(AllergyDTO dto) {

        // 1. Buscar usuario
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        // 2. Crear entidad
        Allergy allergy = toEntity(dto);

        // 3. Asociar usuario
        allergy.setUser(user);

        // 4. Guardar en BD
        Allergy saved = allergyRepository.save(allergy);

        // 5. Devolver DTO
        return toDTO(saved);
    }

    @Transactional
    public void addAllergy_NoLock(Integer userId, String allergyName) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        if (user.getAllergies().size() >= 3) {
            throw new RuntimeException("Max allergies reached");
        }

        sleep(5);

        Allergy allergy = new Allergy();
        allergy.setName(allergyName);
        allergy.setUser(user);

        allergyRepository.save(allergy);
    }

    @Transactional
    public void addAllergy_Pessimistic(Long userId, String allergyName) {

        User user = userRepository.findByIdForUpdate(userId);

        if (user.getAllergies().size() >= 3) {
            throw new RuntimeException("Max allergies reached");
        }

        sleep(2);

        Allergy allergy = new Allergy();
        allergy.setName(allergyName);
        allergy.setUser(user);

        allergyRepository.save(allergy);
    }

    @Transactional
    public void addAllergy_Optimistic(Integer userId, String allergyName) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        if (user.getAllergies().size() >= 3) {
            throw new RuntimeException("Max allergies reached");
        }

        sleep(2);

        Allergy allergy = new Allergy();
        allergy.setName(allergyName);
        allergy.setUser(user);

        allergyRepository.save(allergy);
    }

    // =========================
    // 🔄 MAPPERS
    // =========================

    private AllergyDTO toDTO(Allergy allergy) {
        return AllergyDTO.builder()
                .id(allergy.getId())
                .name(allergy.getName())
                .userId(allergy.getUser().getId())
                .build();
    }

    private Allergy toEntity(AllergyDTO dto) {
        return Allergy.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}