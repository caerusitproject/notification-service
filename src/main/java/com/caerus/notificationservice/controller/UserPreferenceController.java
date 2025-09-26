package com.caerus.notificationservice.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caerus.notificationservice.entity.UserPreference;
import com.caerus.notificationservice.service.UserPreferenceService;
@RestController
@RequestMapping("/api/user-preferences")
public class UserPreferenceController {
	
	@Autowired
	private UserPreferenceService userPreferenceService;
	
	@PostMapping
	public ResponseEntity<?> createUserPreference(@RequestBody UserPreference userPreference) {
		userPreferenceService.saveUserPreference(userPreference);
		return ResponseEntity.ok("User preference saved successfully");
	}
	

}
