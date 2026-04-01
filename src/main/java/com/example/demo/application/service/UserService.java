package com.example.demo.application.service;

import com.example.demo.entity.Allergy;
import com.example.demo.entity.User;
import com.example.demo.model.UserDTO;
import com.example.demo.repository.AllergyRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AllergyRepository allergyRepository;

    // 🔹 GET paginado
    public List<UserDTO> getPaginatedUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 GET by ID
    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::toDTO);
    }

    // 🔹 CREATE
    public String saveUser(UserDTO dto) {
        User user = toEntity(dto);
        User saved = userRepository.save(user);
        return "User created with ID: " + saved.getId();
    }

    // 🔹 DELETE
    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    // 🔹 UPDATE
    public Optional<UserDTO> updateUserById(Integer id, UserDTO newUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(newUser.getName());
                    existingUser.setAge(newUser.getAge());
                    return userRepository.save(existingUser);
                })
                .map(this::toDTO);
    }

    // 🔹 SEARCH
    public List<UserDTO> findUsersByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }



    public void createUserWithAllergiesAndFail(UserDTO dto) {

        // 1. Guardar usuario
        User user = toEntity(dto);
        User savedUser = userRepository.save(user);

        // 2. Crear primera allergy (OK)
        Allergy allergy1 = new Allergy();
        allergy1.setName("Polen");
        allergy1.setUser(savedUser);
        allergyRepository.save(allergy1);

        // 3. Forzar error 💥
        if (true) {
            throw new RuntimeException("💣 Simulación de fallo");
        }

        // 4. Esta nunca se ejecuta
        Allergy allergy2 = new Allergy();
        allergy2.setName("Gluten");
        allergy2.setUser(savedUser);
        allergyRepository.save(allergy2);
    }

    @Transactional
    public void createUserWithAllergiesAndFailTransactional(UserDTO dto) {

        // 1. Guardar usuario
        User user = toEntity(dto);
        User savedUser = userRepository.save(user);

        // 2. Crear primera allergy (OK)
        Allergy allergy1 = new Allergy();
        allergy1.setName("Polen");
        allergy1.setUser(savedUser);
        allergyRepository.save(allergy1);

        // 3. Forzar error 💥
        if (true) {
            throw new RuntimeException("💣 Simulación de fallo");
        }

        // 4. Esta nunca se ejecuta
        Allergy allergy2 = new Allergy();
        allergy2.setName("Gluten");
        allergy2.setUser(savedUser);
        allergyRepository.save(allergy2);
    }

    // =========================
    // 🔄 MAPPERS
    // =========================

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .allergies(null)
                .build();
    }

    private User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .age(dto.getAge())
                .build();
    }
}