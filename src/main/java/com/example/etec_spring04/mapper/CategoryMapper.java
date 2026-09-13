package com.example.etec_spring04.mapper;

import org.springframework.stereotype.Component;

import com.example.etec_spring04.dto.Request.CategoryRequest;
import com.example.etec_spring04.dto.Response.CategoryResponse;
import com.example.etec_spring04.entity.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
        //Or use set[setName, setDescription, setCreatedAt, setUpdatedAt] via create new obj;
    }

    public void updateEntity(Category category, CategoryRequest request) {
        if (category == null || request == null) {
            return;
        }
        category.setName(request.getName());
        category.setDescription(request.getDescription());
    }
}
