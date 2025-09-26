package com.caerus.notificationservice.service;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.caerus.notificationservice.dto.NotificationMessage;
import com.caerus.notificationservice.dto.UserEventDTO;
import com.caerus.notificationservice.entity.UserPreference;
import com.caerus.notificationservice.repository.UserPreferenceRepository;

@Service
public class UserEventConsumer {
//
//    private final UserPreferenceRepository userRepository;
//
//    public UserEventConsumer(UserPreferenceRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @KafkaListener(topics = "${app.kafka.topic.user-registered}", groupId = "${spring.application.fullName}")
//    public void consume(NotificationMessage eventMessage) {
//        System.out.println("📩 Received Kafka Event: " + eventMessage);
//
//        UserEventDTO userEvent = new ModelMapper().map(eventMessage, UserEventDTO.class);
//        UserPreference userPref = new UserPreference();
//        try {
//			userPref.setEmail(userEvent.getEmail());
//			userPref.setPhone(userEvent.getPhoneNumber());
//			userPref.setWhatsappNumber(userEvent.getPhoneNumber());
//			userRepository.save(userPref);
//
//        }catch(Exception ex) {
//        	ex.printStackTrace();
//        }
//        System.out.println("✅ User saved from Kafka event: " + userPref.toString());
//    }
}