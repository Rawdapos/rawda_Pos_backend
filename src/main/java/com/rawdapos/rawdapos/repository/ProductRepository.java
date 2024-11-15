package com.rawdapos.rawdapos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rawdapos.rawdapos.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product findByName(String name);
    public Product findByDescription(String description);
    public Product findByPrice(double price);
    public Product findByQuantity(int quantity);
    
}
