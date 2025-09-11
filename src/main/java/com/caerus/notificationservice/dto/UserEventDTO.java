package com.caerus.notificationservice.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDTO {
	    private Long userId;
	    private String email;
	    private String subject;
	    private String message;
	    private String phoneNumber;
	    private String whatsappNumber;

    // Getters and Setters
}