package com.caerus.notificationservice.processor;

import java.util.*;


import org.springframework.stereotype.Service;

import com.caerus.notificationservice.client.UserServiceClient;
import com.caerus.notificationservice.dto.UserResponse;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class NotificationDataProvider {

    private UserServiceClient userServiceClient;

    public Map<String, Object> getDataForEventData(String eventName, Long entityId) {
        Map<String, Object> data = new HashMap<>();

        switch (eventName) {
            case "USER_REGISTERED" -> {
                UserResponse user = userServiceClient.getUserById(entityId);
                data.put("name", user.name());
                data.put("username", user.username());
                //data.put("loginUrl", "https://myapp.com/login");
            }
            case "PASSWORD_RESET" -> {
                UserResponse user = userServiceClient.getUserById(entityId);
                data.put("name", user.name());
                data.put("resetLink", "https://myapp.com/reset?token=xyz");
            }
            default -> data.put("message", "No additional details available.");
        }
        return data;
    }
}