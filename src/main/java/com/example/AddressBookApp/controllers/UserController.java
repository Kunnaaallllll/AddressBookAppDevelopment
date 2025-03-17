package com.example.AddressBookApp.controllers;

import com.example.AddressBookApp.dto.LoginDTO;
import com.example.AddressBookApp.dto.RegisterDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.service.UserInterface;
import com.example.AddressBookApp.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j  // Lombok Logging
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserInterface userInterface;

    @Autowired
    RedisService redisService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseDTO<String, String> registerUser(@Valid @RequestBody RegisterDTO registerUser) {
        log.info("Received registration request for user: {}", registerUser.getEmail());
        ResponseDTO<String, String> response = userInterface.registerUser(registerUser);
        log.info("Registration response: {}", response.getMessageData());
        return response;
    }

    @PostMapping("/login")
    public ResponseDTO<String, String> loginUser(@Valid @RequestBody LoginDTO loginUser) throws JsonProcessingException {
        log.info("Received login request for user: {}", loginUser.getEmail());
        Object cachedUser = redisService.getFromCache(loginUser.getEmail());
        if(cachedUser != null){
            log.info("User already logged in! Returning cached session.");
            System.out.println(cachedUser);
            ResponseDTO<String, String> res = new ResponseDTO<>();
            res.setMessage("message");
            res.setMessageData("User Already Logged In");
            return res;
        }
        ResponseDTO<String, String> response = userInterface.loginUser(loginUser);
        if (response.getMessage().equals("message")) {
            // 🔹 Store session in Redis for 30 min
            redisService.saveToCache(loginUser.getEmail(), "LOGGED_IN");
            log.info("User session stored in Redis: {}", loginUser.getEmail());
        }
        log.info("Login response: {}", response.getMessageData());
        return response;
    }

    @PutMapping("/forgotPassword/{email}")
    public ResponseDTO<String, String> forgotPassword(@PathVariable String email, @RequestBody Map<String, String> request) {
        log.warn("Forgot password request received for email: {}", email);
        String newPassword = request.get("password");
        ResponseDTO<String, String> response = userInterface.forgotPassword(email, newPassword);
        log.info("Forgot password response: {}", response.getMessageData());
        return response;
    }

    @PutMapping("/resetPassword/{email}")
    public ResponseDTO<String, String> resetPassword(
            @PathVariable String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        log.warn("Reset password request received for email: {}", email);
        ResponseDTO<String, String> response = userInterface.resetPassword(email, currentPassword, newPassword);
        log.info("Reset password response: {}", response.getMessageData());
        return response;
    }

    @PostMapping("/logout/{id}")
    public ResponseDTO<String, String> logoutUser(@PathVariable Long id){
        redisService.removeFromCache(id.toString());
        return userInterface.logoutUser(id);
    }
}