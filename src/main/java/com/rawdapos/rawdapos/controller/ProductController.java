package com.rawdapos.rawdapos.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rawdapos.rawdapos.models.Product;
import com.rawdapos.rawdapos.payload.ProductRequest;
import com.rawdapos.rawdapos.repository.ProductRepository;

import jakarta.validation.Valid;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addProduct(
            @Valid @RequestBody Product product,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var response = new HashMap<String, Object>();
            var anotherProduct = productRepository.findByName(product.getName());
            if (anotherProduct != null) {
                return ResponseEntity.badRequest().body("Product already exists");
            }
            var newProduct = productRepository.save(product);
            response.put("product", newProduct);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred:" + e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list(Authentication authentication) {
        if (authentication == null) {
            System.err.println(authentication.getAuthorities());
            return ResponseEntity.badRequest().body("Unauthenticated");
        }
        var response = new HashMap<String, Object>();
        var products = productRepository.findAll();
        response.put("products", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Integer id) {
        var response = new HashMap<String, Object>();
        var product = productRepository.findById(id);
        response.put("product", product);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest product,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var response = new HashMap<String, Object>();
            var existingProduct = productRepository.findById(id);
            if (existingProduct.isEmpty()) {
                return ResponseEntity.badRequest().body("Product does not exist");
            }
            var newProduct = existingProduct.get();
            newProduct.setName(product.getName());
            newProduct.setDescription(product.getDescription());
            newProduct.setPrice(product.getPrice());
            newProduct.setQuantity(product.getQuantity());
            productRepository.save(newProduct);
            response.put("product", newProduct);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred:" + e);
        }
    }

}
