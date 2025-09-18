package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.models.Account;
import com.example.imageprocessingservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api")
class MainController {
    private final UserService userService;

    MainController(UserService userService) {
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

    @Operation(summary = "Returns the valid CSRF token to use in queries")
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        // returning the token makes Spring Security create and send the cookie
        return token;
    }
}
