package com.caerus.notificationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "notification_templates")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // EMAIL, SMS, WHATSAPP

    @Column(nullable = false)
    private String templateName; // unique template name
    
    @Column(nullable = false)
    private String notificationType; // e.g., WELCOME, PASSWORD_RESET
    
    @Column(nullable = false)
    private String EventType; // e.g., USER_REGISTRATION, FORGOT_PASSWORD`

    @Column(nullable = true, length = 2000)
    @JsonIgnore
    private String content; // template content with placeholders
    
    @Column(nullable = true)
    private String filePath; // path to the template file
    
    private String subject; // subject for email templates
    
    private String contentType; // e.g., TEXT, HTML (for email)

    private String description; // optional description
    
    
}
