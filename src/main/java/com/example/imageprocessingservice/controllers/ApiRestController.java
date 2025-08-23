package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.models.Account;
import com.example.imageprocessingservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api")
class ApiRestController {
    private final UserService userService;

    ApiRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            String username = authentication.getName();
            return "API: Hello " + username + "!";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/login")
    String login() {
        return "Login endpoint";
    }

    @PostMapping("/register")
    String register(@RequestBody Account account) {
        return userService.registerUser(account)
                ? "API: Registration successful!"
                : "Registration failed. User already exists.";
    }
}
