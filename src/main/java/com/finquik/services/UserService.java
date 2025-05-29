package com.finquik.services;

import com.finquik.DTOs.UserRegistrationRequest;
import com.finquik.DTOs.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest registrationRequest);
}