package com.example.etec_spring04.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.etec_spring04.config.client.CloudinaryService;
import com.example.etec_spring04.dto.Request.ProductRequest;
import com.example.etec_spring04.dto.Response.ProductResponse;
import com.example.etec_spring04.entity.Category;
import com.example.etec_spring04.entity.Product;
import com.example.etec_spring04.exception.BadRequestException;
import com.example.etec_spring04.exception.ResourceNotFoundException;
import com.example.etec_spring04.mapper.ProductMapper;
import com.example.etec_spring04.repository.CategoryRepository;
import com.example.etec_spring04.repository.ProductRepository;
import com.example.etec_spring04.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        MultipartFile file = request != null ? request.getFile() : null;
        return createProductWithImage(request, file);
    }

    @Override
    @Transactional
    public ProductResponse createProductWithImage(ProductRequest request, MultipartFile file) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        if (productRepository.existsByName(request.getName())) {
            throw new BadRequestException("Product with name '" + request.getName() + "' already exists");
        }

        Product product = productMapper.toEntity(request, category);

        MultipartFile uploadFile = (file != null && !file.isEmpty()) ? file : (request.getFile() != null && !request.getFile().isEmpty() ? request.getFile() : null);
        if (uploadFile != null) {
            Map<?, ?> uploadResult = cloudinaryService.uploadFile(uploadFile, "ETEC_SpringBoot04_Cloudinary");
            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            product.setImage_url(imageUrl);
            product.setPublic_id(publicId);
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        }

        if (!product.getName().equalsIgnoreCase(request.getName()) && productRepository.existsByName(request.getName())) {
            throw new BadRequestException("Product with name '" + request.getName() + "' already exists");
        }

        productMapper.updateEntity(product, request, category);

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            if (product.getPublic_id() != null && !product.getPublic_id().isBlank()) {
                cloudinaryService.deleteFile(product.getPublic_id());
            }
            Map<?, ?> uploadResult = cloudinaryService.uploadFile(request.getFile(), "ETEC_SpringBoot04_Cloudinary");
            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            product.setImage_url(imageUrl);
            product.setPublic_id(publicId);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProductImage(Long id, MultipartFile file) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getPublic_id() != null && !product.getPublic_id().isBlank()) {
            cloudinaryService.deleteFile(product.getPublic_id());
        }

        Map<?, ?> uploadResult = cloudinaryService.uploadFile(file, "ETEC_SpringBoot04_Cloudinary");
        String imageUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        product.setImage_url(imageUrl);
        product.setPublic_id(publicId);

        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getPublic_id() != null && !product.getPublic_id().isBlank()) {
            cloudinaryService.deleteFile(product.getPublic_id());
        }

        productRepository.delete(product);
    }
}
