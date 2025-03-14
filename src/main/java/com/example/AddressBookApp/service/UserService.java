package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.RegisterDTO;
import com.example.AddressBookApp.model.User;
import com.example.AddressBookApp.repository.UserRepository;
import com.example.AddressBookApp.service.UserInterface;
import com.example.AddressBookApp.utility.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j  // Lombok Logging
@Service
public class UserService implements UserInterface {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    JwtUtility jwtUtility;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResponseEntity<Map<String, String>> registerUser(RegisterDTO registerDTO) {
        log.info("Registering user: {}", registerDTO.getEmail());
        Map<String, String> res = new HashMap<>();

        if (existsByEmail(registerDTO.getEmail())) {
            log.warn("Registration failed: User already exists with email {}", registerDTO.getEmail());
            res.put("error", "User Already Exists");
            return ResponseEntity.ok(res);
        }

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User {} registered successfully!", user.getEmail());

        emailService.sendEmail(user.getEmail(), "Welcome to Our Platform!",
                "Hello " + user.getFullName() + ",\n\nYour account has been successfully created!");

        res.put("message", "User Registered Successfully");
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<Map<String, String>> loginUser(LoginDTO loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getEmail());
        Map<String, String> res = new HashMap<>();
        Optional<User> userExists = getUserByEmail(loginDTO.getEmail());

        if (userExists.isPresent()) {
            User user = userExists.get();
            if (matchPassword(loginDTO.getPassword(), user.getPassword())) {
                String token = jwtUtility.generateToken(user.getEmail());
                user.setToken(token);
                userRepository.save(user);
                log.debug("Login successful for user: {} - Token generated", user.getEmail());

                emailService.sendEmail(user.getEmail(), "Welcome Back!",
                        "Hello " + user.getFullName() + ",\n\nYou have successfully logged in. Your token: " + token);

                res.put("message", "User Logged In Successfully: " + token);
                return ResponseEntity.ok(res);
            } else {
                log.warn("Invalid credentials for user: {}", loginDTO.getEmail());
                res.put("error", "Invalid Credentials");
                return ResponseEntity.ok(res);
            }
        } else {
            log.error("User not found with email: {}", loginDTO.getEmail());
            res.put("error", "User Not Found");
            return ResponseEntity.ok(res);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        log.debug("Matching password for login attempt");
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public ResponseEntity<Map<String, String>> forgotPassword(String email, String newPassword) {
        log.warn("Forgot password request received for email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOptional.isEmpty()) {
            log.error("Forgot password failed: User not found with email: {}", email);
            response.put("message", "Sorry! We cannot find the user email: " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password updated successfully for user: {}", email);

        emailService.sendEmail(email, "Password Reset", "Your password has been changed successfully!");
        response.put("message", "Password has been changed successfully!");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, String>> resetPassword(String email, String currentPassword, String newPassword) {
        log.warn("Reset password request received for email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (userOptional.isEmpty()) {
            log.error("Reset password failed: User not found with email: {}", email);
            response.put("message", "User not found with email: " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.warn("Reset password failed: Incorrect current password for user: {}", email);
            response.put("message", "Current password is incorrect!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", email);

        response.put("message", "Password reset successfully!");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Map<String, String>> logoutUser(Long id){
        Map<String, String> hm = new HashMap<>();
        User userExists = userRepository.findById(id).orElse(null);
        if(userExists == null){
            hm.put("error","User Not Found For ID: "+id);
            return ResponseEntity.ok(hm);
        }
        userExists.setToken(null);
        userRepository.save(userExists);
        hm.put("message","User Logged Out Successfully for ID: "+id);
        return ResponseEntity.ok(hm);
    }
}