package com.example.demo.dto;
public class MenuItemDto {
private Integer id;
private String name;
private double price;

public MenuItemDto() {}

public MenuItemDto(Integer id, String name, double price) {
    this.id = id;
    this.name = name;
    this.price = price;
}

public Integer getId() { return id; }
public String getName() { return name; }
public double getPrice() { return price; }


public void setId(Integer id) { this.id = id; }
public void setName(String name) { this.name = name; }
public void setPrice(double price) { this.price = price; }
}


