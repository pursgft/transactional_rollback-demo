package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.AllergyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(UserRepository userRepository,
                               AllergyRepository allergyRepository) {
		return args -> {
				User user = User.builder().name("manolin").age(50).allergies(List.of()).build();
				userRepository.save(user);
		};

	}
}
