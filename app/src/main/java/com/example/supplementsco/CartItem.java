package com.example.supplementsco;

public class CartItem {
    public String name;
    public Double price;
    public int quantity;

    public CartItem() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartItem(String name, int quantity, Double price){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}