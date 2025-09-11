package com.caerus.notificationservice.service;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.caerus.notificationservice.dto.UserEventDTO;
import com.caerus.notificationservice.model.UserPreference;
import com.caerus.notificationservice.repo.UserPreferenceRepository;
import com.caerus.notificationservice.util.UserMapper;

@Service
public class UserEventConsumer {

    private final UserPreferenceRepository userRepository;

    public UserEventConsumer(UserPreferenceRepository userRepository) {
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "user.notification", groupId = "usergroup")
    public void consume(String content) {
    	UserEventDTO eventDto =null;
    	UserPreference userPreference = null;
		try {
			ModelMapper modelMapper = new ModelMapper();
			eventDto = modelMapper.map(content, UserEventDTO.class);
			UserPreference userPref = new UserPreference();
			userPref.setEmail(eventDto.getEmail());
			userPref.setPhone(eventDto.getPhoneNumber());
			userPref.setWhatsappNumber(eventDto.getWhatsappNumber());
			userRepository.save(userPreference);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        System.out.println("✅ User saved from Kafka event: " + userPreference.toString());
    }
}