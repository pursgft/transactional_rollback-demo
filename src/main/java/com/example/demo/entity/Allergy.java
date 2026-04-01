package com.example.demo.entity;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.*;

@Builder
@Entity
@Table(name = "allergies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
