package com.example.imageprocessingservice.controllers;

import com.example.imageprocessingservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
class WebController {
    private final UserService userService;
    WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            String username = authentication.getName();
            model.addAttribute("username", username);
            return "/web/index";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/login")
    String login() {
        return "/web/login";
    }

    @GetMapping("/register")
    String register() {
        return "/web/register";
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
