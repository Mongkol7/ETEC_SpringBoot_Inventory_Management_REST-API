package com.example.etec_spring04.mapper;

import org.springframework.stereotype.Component;

import com.example.etec_spring04.dto.Request.ProductRequest;
import com.example.etec_spring04.dto.Response.ProductResponse;
import com.example.etec_spring04.entity.Category;
import com.example.etec_spring04.entity.Product;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request, Category category) {
        if (request == null) {
            return null;
        }
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .stock(request.getStock())
                .price(request.getPrice())
                .category(category)
                .build();
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .image_url(product.getImage_url())
                .public_id(product.getPublic_id())
                .stock(product.getStock())
                .price(product.getPrice())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public void updateEntity(Product product, ProductRequest request, Category category) {
        if (product == null || request == null) {
            return;
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setStock(request.getStock());
        product.setPrice(request.getPrice());
        if (category != null) {
            product.setCategory(category);
        }
    }
}
