package com.finquik.services;

import com.finquik.DTOs.UserRegistrationRequest;
import com.finquik.DTOs.UserResponse;
import com.finquik.common.exceptions.EmailAlreadyExistsException;
import com.finquik.models.User;
import com.finquik.models.Category;
import com.finquik.models.CategoryType;
import com.finquik.repositories.CategoryRepository;
import com.finquik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finquik.common.exceptions.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Error: Email '" + registrationRequest.getEmail() + "' is already taken!");
        }

        User user = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        // creates default categories (one for income and one for expenses) for the new user
        createDefaultCategoriesForUser(savedUser);

        return UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // Auxiliary method to create default categories for a new user
    private void createDefaultCategoriesForUser(User user) {
        Category uncategorizedExpense = Category.builder()
                .name("Uncategorized")
                .type(CategoryType.EXPENSE)
                .user(user)
                .build();

        Category uncategorizedIncome = Category.builder()
                .name("Uncategorized")
                .type(CategoryType.INCOME)
                .user(user)
                .build();

        categoryRepository.save(uncategorizedExpense);
        categoryRepository.save(uncategorizedIncome);
    }
}