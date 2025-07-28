package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class AuthController {
    private final UserService userService;
    AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/register")
    String register() {
        return "register";
    }

    @PostMapping("/register")
    ResponseEntity<String> registerUser(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password) {
        System.out.println(username + " " + password + " registered successfully!");

        if (userService.registerUser(username, password)) {
            return ResponseEntity.ok("Registration successful!");
        } else {
            return new ResponseEntity<>("Registration failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
