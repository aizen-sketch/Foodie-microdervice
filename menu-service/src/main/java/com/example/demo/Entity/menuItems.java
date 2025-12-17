package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_items")
public class menuItems {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "menu_id")
private int menuId;
@Column(nullable = false)
private String name;

@Column(nullable = false)
private double price;

@Lob
@Column(columnDefinition = "LONGBLOB")
private byte[] image;

private String imageType; // image/png, image/jpeg

public byte[] getImage() {
	return image;
}
public void setImage(byte[] image) {
	this.image = image;
}
public String getImageType() {
	return imageType;
}
public void setImageType(String imageType) {
	this.imageType = imageType;
}
public int getMenuId() {
	return menuId;
}
public void setMenuId(int menuId) {
	this.menuId = menuId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public double getPrice() {
	return price;
}
public void setPrice(double price) {
	this.price = price;
}
public menuItems(String name, double price) {
	super();
	this.name = name;
	this.price = price;
}
public menuItems() {
	super();
	// TODO Auto-generated constructor stub
}


}