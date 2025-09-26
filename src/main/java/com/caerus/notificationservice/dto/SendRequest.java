package com.caerus.notificationservice.dto;

import java.util.Map;

import com.caerus.notificationservice.enums.Channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SendRequest {
    @NotBlank
    private Long userId;
    @NotNull
    private Channel channel;
    @NotBlank
    private String content;
    private String email; // Optional, used for email
    private String fullName; // Optional, used for personalization
	private String subject; // Optional, used for email
	private String phoneNumber; // Optional, used for SMS and WhatsApp
	private String templateName;
	private String templateType;
	private Map<String, Object> data;
	
}
