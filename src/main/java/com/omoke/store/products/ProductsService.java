package com.omoke.store.products;

import com.omoke.store.exceptions.CategoryNotFoundException;
import com.omoke.store.exceptions.NotFoundException;
import com.omoke.store.mappers.ProductMapper;
import com.omoke.store.products.dtos.ProductDto;
import com.omoke.store.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductsService {
    private final ProductMapper productMapper;
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> findAllProducts(Byte categoryId) {
        List<Product> products;

        if (categoryId != null) {
            products = productsRepository.findByCategoryId(categoryId);
        } else {
            products = productsRepository.findAllWithCategory();
        }

        return products.stream().map(productMapper::toDto).toList();
    }

    public ProductDto findProductById(Long productId) {
        var product = productsRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product with ID " + productId + " not found")
        );

        return  productMapper.toDto(product);
    }

    public ProductDto createProduct(ProductDto request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException("Category with ID " + request.getCategoryId() + " not found")
        );

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productsRepository.save(product);

        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long productId, ProductDto request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException("Category with ID " + request.getCategoryId() + " not found")
        );

        var product = productsRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product with ID " + productId + " not found")
        );

        productMapper.update(request, product);
        product.setCategory(category);
        productsRepository.save(product);

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long productId) {
        var product = productsRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product with ID " + productId + " not found")
        );

        productsRepository.delete(product);
    }
}
