package com.classroom.equipment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<Object> getRoot() {
        return ResponseEntity.ok("Welcome");
    }

    @GetMapping("ping")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok("pong");
    }
}
