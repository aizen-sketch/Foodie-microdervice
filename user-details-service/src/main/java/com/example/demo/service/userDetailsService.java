package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.details;
import com.example.demo.repository.userDetailsRepository;

@Service
public class userDetailsService {
	
	@Autowired
	userDetailsRepository detailsRepo;

	public details addDetails(Long userId, details userDetails) {
		details details = detailsRepo.save(userDetails);
		return details;
	}
	
	public details getDetailsById(Long userId) {
		return detailsRepo.findByUserId(userId).orElse(null);
	}

	public details updateDetailsById(Long userId, details updatedDetails) {
		details existing = detailsRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Details not found for userId: " + userId));

        // update fields
		existing.setName(updatedDetails.getName());
        existing.setEmail(updatedDetails.getEmail());
        existing.setPhone(updatedDetails.getPhone());
        existing.setAddress(updatedDetails.getAddress());

        return detailsRepo.save(existing);
		
	}

}
