package com.example.etec_spring04.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.etec_spring04.dto.Request.ProductRequest;
import com.example.etec_spring04.dto.Response.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse createProductWithImage(ProductRequest request, MultipartFile file);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    List<ProductResponse> getProductsByCategoryId(Long categoryId);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse updateProductImage(Long id, MultipartFile file);

    void deleteProduct(Long id);
}
