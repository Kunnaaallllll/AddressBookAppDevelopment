package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.RegisterDTO;
import com.example.AddressBookApp.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public interface UserInterface {
    ResponseEntity<Map<String,String>> registerUser(RegisterDTO registerDTO);
    ResponseEntity<Map<String,String>> loginUser(LoginDTO loginDTO);
    boolean existsByEmail(String email);
    Optional<User> getUserByEmail(String email);
    boolean matchPassword(String rawPassword, String encodedPassword);
    ResponseEntity<Map<String, String>> forgotPassword(String email, String newPassword);
    ResponseEntity<Map<String, String>> resetPassword(String email, String currentPassword, String newPassword);
}