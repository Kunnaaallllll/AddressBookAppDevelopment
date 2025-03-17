package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.RegisterDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.model.User;
import com.example.AddressBookApp.repository.UserRepository;
import com.example.AddressBookApp.service.UserInterface;
import com.example.AddressBookApp.utility.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
    public ResponseDTO<String, String> registerUser(RegisterDTO registerDTO) {
        log.info("Registering user: {}", registerDTO.getEmail());
        ResponseDTO<String, String> res = new ResponseDTO<>();
        if (existsByEmail(registerDTO.getEmail())) {
            log.warn("Registration failed: User already exists with email {}", registerDTO.getEmail());
            res.setMessage("error");
            res.setMessageData("User Already Exists");

            return res;
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

        res.setMessage("message");
        res.setMessageData("User Registered Successfully");
        return res;
    }

    @Override
    public ResponseDTO<String, String> loginUser(LoginDTO loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getEmail());
        ResponseDTO<String, String> res = new ResponseDTO<>();
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
                res.setMessage("message");
                res.setMessageData("User Logged In Successfully: " + token);
                return res;
            } else {
                log.warn("Invalid credentials for user: {}", loginDTO.getEmail());
                res.setMessage("error");
                res.setMessageData("Invalid Crendentials");
                return res;
            }
        } else {
            log.error("User not found with email: {}", loginDTO.getEmail());
            res.setMessage("error");
            res.setMessageData("User Not Found");
            return res;
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
    public ResponseDTO<String, String> forgotPassword(String email, String newPassword) {
        log.warn("Forgot password request received for email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        ResponseDTO<String, String> res = new ResponseDTO<>();

        if (userOptional.isEmpty()) {
            log.error("Forgot password failed: User not found with email: {}", email);
            res.setMessage("error");
            res.setMessageData("Sorry! We cannot find the user email: " + email);
            return res;
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password updated successfully for user: {}", email);

        emailService.sendEmail(email, "Password Reset", "Your password has been changed successfully!");
        res.setMessage("message");
        res.setMessageData("Password has Been Updated Successfully");
        return res;
    }

    @Override
    public ResponseDTO<String, String> resetPassword(String email, String currentPassword, String newPassword) {
        log.warn("Reset password request received for email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        ResponseDTO<String, String> res = new ResponseDTO<>();

        if (userOptional.isEmpty()) {
            log.error("Reset password failed: User not found with email: {}", email);
            res.setMessage("error");
            res.setMessageData("User not found with email: " + email);
            return res;
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.warn("Reset password failed: Incorrect current password for user: {}", email);
            res.setMessage("error");
            res.setMessageData("Current Password is Incorrect");
            return res;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", email);

        res.setMessage("message");
        res.setMessageData("Password Reset Successfully");
        return res;
    }

    @Override
    public ResponseDTO<String, String> logoutUser(Long id){
        ResponseDTO<String, String> res = new ResponseDTO<>();
        User userExists = userRepository.findById(id).orElse(null);
        if(userExists == null){
            res.setMessage("error");
            res.setMessageData("User Not Found For ID: "+id);
            return res;
        }
        userExists.setToken(null);
        userRepository.save(userExists);
        res.setMessage("message");
        res.setMessageData("User Logged Out Successfully for ID: "+id);
        return res;
    }
}