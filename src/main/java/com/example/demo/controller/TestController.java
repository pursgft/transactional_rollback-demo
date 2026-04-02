package com.example.demo.controller;

import com.example.demo.application.service.AllergyService;
import com.example.demo.application.service.UserService;
import com.example.demo.entity.Allergy;
import com.example.demo.entity.User;
import com.example.demo.model.AllergyDTO;
import com.example.demo.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserDTO> getUsers(@RequestParam int page, @RequestParam int size) {
        return userService.getPaginatedUsers(page, size);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        Optional<UserDTO> userById = userService.getUserById(id);
        
        return userById
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    public String saveUser(@RequestBody UserDTO usuario) {
        return userService.saveUser(usuario);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        if(userService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO newUser) {
        Optional<UserDTO> updatedUser = userService.updateUserById(id, newUser);

        return updatedUser
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // /user/search?name=pepe

    @GetMapping("/user/search")
    public ResponseEntity<List<UserDTO>> findUsersByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findUsersByName(name));
    }

    @Autowired
    private AllergyService allergyService;

    // 🔹 GET allergy por ID
    @GetMapping("/allergy/{id}")
    public ResponseEntity<AllergyDTO> getAllergyById(@PathVariable Integer id) {
        Optional<AllergyDTO> allergy = allergyService.getAllergyById(id);

        return allergy
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 POST allergy
    @PostMapping("/allergy")
    public ResponseEntity<AllergyDTO> saveAllergy(@RequestBody AllergyDTO allergyDTO) {
        AllergyDTO saved = allergyService.saveAllergy(allergyDTO);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/user/with-allergies-fail")
    public ResponseEntity<String> testTransactionFail(@RequestBody UserDTO dto) {
        userService.createUserWithAllergiesAndFail(dto);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/user/with-allergies-fail-transactional")
    public ResponseEntity<String> testTransaction(@RequestBody UserDTO dto) throws Exception {
        userService.createUserWithAllergiesAndFailTransactional(dto);
        return ResponseEntity.ok("OK");
    }

    @PutMapping("/user/test-isolation/{id}")
    public String testIsolation(@PathVariable Integer id) throws InterruptedException {
        return userService.testIsolation(id);
    }

    @PostMapping("/user/age-test")
    public String ageTest(@RequestBody User user) throws InterruptedException {
        return userService.testEdad(user);
    }
}
