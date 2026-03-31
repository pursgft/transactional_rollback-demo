package com.example.demo.application.service;

import com.example.demo.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    public List<UserDTO> users = new ArrayList<>();
    private Integer id = 0;

    public String saveUser(UserDTO usuario) {
        boolean userExists = users.stream().anyMatch(user -> user.getId().equals(usuario.getId()));
        if(!userExists) {
            usuario.setId(id++);
            users.add(usuario);
        }
        return "ok";
    }

    public Optional<UserDTO> getUserById(Integer id) {
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst();
    }

    public boolean deleteUser(Integer id) {
        return users.removeIf(user -> id.equals(user.getId()));
    }

    public Optional<UserDTO> updateUserById(Integer id, UserDTO usuario) {
        if(deleteUser(id)) {
            saveUser(usuario);
            return Optional.of(usuario);
        }
        return Optional.empty();
    }

    public List<UserDTO> findUsersByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .toList();
    }

    public List<UserDTO> getPaginatedUsers(int page, int size) {
        int start = page * size;

        return users.stream()
                .skip(start)
                .limit(size)
                .toList();
    }
}
