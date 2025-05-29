package com.finquik.controllers;

import com.finquik.DTOs.UserRegistrationRequest;
import com.finquik.DTOs.UserResponse;
import com.finquik.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        UserResponse registeredUser = userService.registerUser(registrationRequest);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // TODO: Implement login functionality
    // @PostMapping("/login")
    // public ResponseEntity<?> authenticateUser(...) { ... }
}