package com.example.orderapp;

public class ProductObject {
    private String id;
    private String name;

    public ProductObject(String id, String name, String price, String code, String description, String photo) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return name;
    }
}
