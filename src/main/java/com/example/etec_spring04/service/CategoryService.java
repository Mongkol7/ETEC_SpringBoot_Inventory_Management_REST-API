package com.example.etec_spring04.service;

import java.util.List;

import com.example.etec_spring04.dto.Request.CategoryRequest;
import com.example.etec_spring04.dto.Response.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}
