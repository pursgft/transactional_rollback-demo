package com.example.demo.repository;

import com.example.demo.entity.Allergy;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Integer> {

    // 🔹 Obtener alergias de un usuario
    List<Allergy> findByUser(User user);

    // 🔹 Alternativa: por id del usuario (más cómoda muchas veces)
    List<Allergy> findByUserId(Integer userId);

    // 🔹 Buscar por nombre
    List<Allergy> findByName(String name);

    // 🔹 Buscar por nombre + usuario
    List<Allergy> findByNameAndUserId(String name, Integer userId);
}