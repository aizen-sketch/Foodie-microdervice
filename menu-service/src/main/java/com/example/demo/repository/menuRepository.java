package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.menuItems;

@Repository
public interface menuRepository extends JpaRepository<menuItems, Integer > {
	@Query(value = "SELECT * FROM menu_items ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<menuItems> findAnyThree();
}