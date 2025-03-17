package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.RegisterDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserInterface {
    ResponseDTO<String, String> registerUser(RegisterDTO registerDTO);
    ResponseDTO<String, String> loginUser(LoginDTO loginDTO);
    boolean existsByEmail(String email);
    Optional<User> getUserByEmail(String email);
    boolean matchPassword(String rawPassword, String encodedPassword);
    ResponseDTO<String, String> forgotPassword(String email, String newPassword);
    ResponseDTO<String, String> resetPassword(String email, String currentPassword, String newPassword);
    ResponseDTO<String, String> logoutUser(Long id);
}