package com.example.demo.model;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String name;
    private Integer age;
    private List<AllergyDTO> allergies;

}
