package com.caerus.notificationservice.service;

import org.springframework.stereotype.Service;

import com.caerus.notificationservice.dto.NotificationMessage;
import com.caerus.notificationservice.model.Channel;
import com.caerus.notificationservice.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("notificationConsumerService")
public class NotificationConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processMessage(String message) {
        try {
            // Convert JSON → Object
        	NotificationMessage notification = objectMapper.readValue(message, NotificationMessage.class);
			System.out.println("✅ Parsed Kafka message: " + notification);	
			Notification userNotification =new Notification();
			userNotification.setSubject(notification.getSubject());
			userNotification.setContent(notification.getMessage());
			userNotification.setChannel(Channel.EMAIL);
			System.out.println("✅ Parsed Kafka message: " + notification);	
            
        } catch (Exception e) {
            System.err.println("❌ Failed to parse Kafka message: " + message);
            e.printStackTrace();
        }
    }
}