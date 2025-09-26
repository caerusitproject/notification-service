package com.caerus.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caerus.notificationservice.entity.UserPreference;
import com.caerus.notificationservice.repository.UserPreferenceRepository;

@Service
public class UserPreferenceService {
	
	@Autowired
	private UserPreferenceRepository userPreferenceRepository;
	
	
	public void saveUserPreference(UserPreference userPreference) {
		userPreferenceRepository.save(userPreference);
		
	}
	

}
