package com.example.quickinv.models;

public class Item {
    private int id;
    private int userId;
    private String name;
    private int quantity;
    private double price;
    private String description;
    private String createdAt;

    public Item() {}

    public Item(int userId, String name, int quantity, double price, String description) {
        this.userId = userId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
    }

    public Item(int id, int userId, String name, int quantity, double price, String description, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
