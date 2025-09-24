package com.finquik.services;

import com.finquik.DTOs.CategoryRequest;
import com.finquik.DTOs.CategoryResponse;
import com.finquik.models.CategoryType;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest categoryRequest, String userEmail);
    List<CategoryResponse> getCategoriesByUser(String userEmail, CategoryType type);
    CategoryResponse getCategoryById(Long categoryId, String userEmail);
    CategoryResponse updateCategory(Long categoryId, CategoryRequest categoryRequest, String userEmail);
    void deleteCategory(Long categoryId, String userEmail);
}
