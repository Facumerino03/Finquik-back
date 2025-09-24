package com.finquik.services;

import com.finquik.common.exceptions.DuplicateResourceException;
import com.finquik.common.exceptions.ResourceNotFoundException;
import com.finquik.DTOs.CategoryRequest;
import com.finquik.DTOs.CategoryResponse;
import com.finquik.models.Category;
import com.finquik.models.User;
import com.finquik.models.CategoryType;
import com.finquik.repositories.CategoryRepository;
import com.finquik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest, String userEmail) {
        User user = findUserByEmail(userEmail);

        // Validates to avoid duplicate category names for the same user and type
        if (categoryRepository.existsByNameAndUserAndType(categoryRequest.getName(), user, categoryRequest.getType())) {
            throw new DuplicateResourceException("Category with name '" + categoryRequest.getName() + "' and type '" + categoryRequest.getType() + "' already exists.");
        }

        Category category = Category.builder()
                .name(categoryRequest.getName())
                .type(categoryRequest.getType())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByUser(String userEmail, CategoryType type) {
        User user = findUserByEmail(userEmail);
        List<Category> categories;

        if (type != null) {
            categories = categoryRepository.findByUserAndType(user, type);
        } else {
            categories = categoryRepository.findByUser(user);
        }

        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long categoryId, String userEmail) {
        User user = findUserByEmail(userEmail);
        Category category = findCategoryByIdAndUser(categoryId, user);
        return mapToCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest, String userEmail) {
        User user = findUserByEmail(userEmail);
        Category categoryToUpdate = findCategoryByIdAndUser(categoryId, user);

        // Validates to avoid duplicate category names for the same user and type when updating
        if (!categoryToUpdate.getName().equalsIgnoreCase(categoryRequest.getName()) &&
                categoryRepository.existsByNameAndUserAndType(categoryRequest.getName(), user, categoryToUpdate.getType())) {
            throw new DuplicateResourceException("Category with name '" + categoryRequest.getName() + "' already exists for this type.");
        }

        categoryToUpdate.setName(categoryRequest.getName());
        // It is not allowed to change the type of a category after creation

        Category updatedCategory = categoryRepository.save(categoryToUpdate);
        return mapToCategoryResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId, String userEmail) {
        User user = findUserByEmail(userEmail);
        Category categoryToDelete = findCategoryByIdAndUser(categoryId, user);

        // TODO: consider validating if the category is in use by any transactions before deletion.

        categoryRepository.delete(categoryToDelete);
    }

    private Category findCategoryByIdAndUser(Long categoryId, User user) {
        return categoryRepository.findByIdAndUser(categoryId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }
}