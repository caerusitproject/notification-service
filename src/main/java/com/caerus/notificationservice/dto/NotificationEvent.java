package com.caerus.notificationservice.dto;

import java.util.List;

public record NotificationEvent(
        Long userId,
        String fullName,
        String eventType,
        String email,
        String resetLink,
        String phoneNumber,
        String whatsappNumber,
        List<String> channels
) {
}
