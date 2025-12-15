package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.details;

@Repository
public interface userDetailsRepository extends JpaRepository<details, Long>{

	Optional<details> findByUserId(Long userId);

}
