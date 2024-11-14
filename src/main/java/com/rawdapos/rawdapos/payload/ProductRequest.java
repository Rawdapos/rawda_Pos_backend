package com.rawdapos.rawdapos.payload;

import jakarta.validation.constraints.*;

public class ProductRequest {


    @Null
    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private double price;

    @NotNull
    private int quantity;

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
