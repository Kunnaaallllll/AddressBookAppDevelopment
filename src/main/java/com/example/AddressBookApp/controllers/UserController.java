package com.example.AddressBookApp.controllers;

import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.RegisterDTO;
import com.example.AddressBookApp.service.UserInterface;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j  // Lombok Logging
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserInterface userInterface;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegisterDTO registerUser) {
        log.info("Received registration request for user: {}", registerUser.getEmail());
        ResponseEntity<Map<String, String>> response = userInterface.registerUser(registerUser);
        log.info("Registration response: {}", response.getBody());
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@Valid @RequestBody LoginDTO loginUser) {
        log.info("Received login request for user: {}", loginUser.getEmail());
        ResponseEntity<Map<String, String>> response = userInterface.loginUser(loginUser);
        log.info("Login response: {}", response.getBody());
        return response;
    }

    @PutMapping("/forgotPassword/{email}")
    public ResponseEntity<Map<String, String>> forgotPassword(@PathVariable String email, @RequestBody Map<String, String> request) {
        log.warn("Forgot password request received for email: {}", email);
        String newPassword = request.get("password");
        ResponseEntity<Map<String, String>> response = userInterface.forgotPassword(email, newPassword);
        log.info("Forgot password response: {}", response.getBody());
        return response;
    }

    @PutMapping("/resetPassword/{email}")
    public ResponseEntity<Map<String, String>> resetPassword(
            @PathVariable String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        log.warn("Reset password request received for email: {}", email);
        ResponseEntity<Map<String, String>> response = userInterface.resetPassword(email, currentPassword, newPassword);
        log.info("Reset password response: {}", response.getBody());
        return response;
    }
}