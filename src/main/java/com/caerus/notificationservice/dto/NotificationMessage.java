package com.caerus.notificationservice.dto;

import java.util.Map;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NotificationMessage {
    private Long userId;
    private String fullName;
    private String email;
    private String eventType;
    private String countryCode;
    private String phoneNumber;
    private String whatsappNumber;
    private String channel;

    
}