package com.classroom.equipment.layer.presentation.controller;

import com.classroom.equipment.utility.response.Response;
import com.classroom.equipment.utility.response.ResponseFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    Response<Object> getRoot() {
        return ResponseFactory.getSuccessResponse("Welcome");
    }

    @GetMapping("ping")
    Response<Object> getAll() {
        return ResponseFactory.getSuccessResponse("pong");
    }
}
