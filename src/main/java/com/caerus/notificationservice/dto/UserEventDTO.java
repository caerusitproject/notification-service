package com.caerus.notificationservice.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDTO {
		private Long userId;
	    private String fullName;
	    private String eventType;
	    private String email;
	    private String phoneNumber;
        private String countryCode;
    // Getters and Setters
}