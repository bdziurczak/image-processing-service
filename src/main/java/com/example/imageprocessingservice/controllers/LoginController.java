package com.example.imageprocessingservice.controllers;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class LoginController {
    @GetMapping("/login")
    String login() {
        return "login";
    }

}
