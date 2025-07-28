package com.example.imageprocessingservice.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    UserService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(String username, String password) {
        if (userDetailsManager.userExists(username)) {
            return false;
        }
        try {
            UserDetails user = User.builder()
                    .username(username)
                    .password("{noop}" + password) // ad hoc, should be .password(passwordEncoder.encode(password))
                    .roles("USER")
                    .build();
            userDetailsManager.createUser(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
