package com.classroom.equipment.controller;

import com.classroom.equipment.entity.User;
import com.classroom.equipment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addStudent(String name, String email) {
        User user = User.builder()
            .name(name)
            .email(email)
            .build();
        userRepository.save(user);
        return ResponseEntity.ok("Student added successfully");
    }
}
