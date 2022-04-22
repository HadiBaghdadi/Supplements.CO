package com.example.supplementsco;

public class Product {
    public String name;
    public String description;
    public String tag;
    public Double price;
    public int id;

    public Product(){

    }

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product(String name, String description, String tag, int id, Double price){
        this.name = name;
        this.description = description;
        this.tag = tag;
        this.price = price;
        this.id = id;
    }



}
