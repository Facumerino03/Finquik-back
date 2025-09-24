package com.finquik.controllers;

import com.finquik.DTOs.CategoryRequest;
import com.finquik.DTOs.CategoryResponse;
import com.finquik.models.CategoryType;
import com.finquik.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest,
            Authentication authentication) {
        String userEmail = authentication.getName();
        CategoryResponse createdCategory = categoryService.createCategory(categoryRequest, userEmail);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getUserCategories(
            Authentication authentication,
            @RequestParam(required = false) CategoryType type) {

        String userEmail = authentication.getName();
        List<CategoryResponse> categories = categoryService.getCategoriesByUser(userEmail, type);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        CategoryResponse category = categoryService.getCategoryById(id, userEmail);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest,
            Authentication authentication) {
        String userEmail = authentication.getName();
        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryRequest, userEmail);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        categoryService.deleteCategory(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}