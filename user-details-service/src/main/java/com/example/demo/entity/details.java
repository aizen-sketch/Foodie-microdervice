package com.example.demo.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class details {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	long detailsId;
	String name;
	long userId;
	String email;
	String phone;
	@Embedded
	Address address;
	public long getDetailsId() {
		return detailsId;
	}
	public void setDetailsId(long detailsId) {
		this.detailsId = detailsId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
