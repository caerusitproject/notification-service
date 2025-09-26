package com.caerus.notificationservice.dto;

public record NotificationEvent(
//        String eventType,
//        String email,
//        String message
        Long userId,
        String fullName,
        String eventType,
        String email
) {
}
