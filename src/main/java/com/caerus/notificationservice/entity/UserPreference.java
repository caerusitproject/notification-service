package com.caerus.notificationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;




@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPreference {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private Long userId;

    private boolean emailEnabled = true;
    private boolean smsEnabled = false;
    private boolean whatsappEnabled = false;
    private boolean inAppEnabled = true;
    private String notificationType;
    private String email;
    private String phone;
    private String whatsappNumber;
	

}
