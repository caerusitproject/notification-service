package com.caerus.notificationservice.dto;

public record NotificationEvent(
        Long userId,
        String fullName,
        String eventType,
        String email,
        String resetLink,
        String phoneNumber,
        String whatsappNumber
) {
}
