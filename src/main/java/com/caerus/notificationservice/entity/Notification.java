package com.caerus.notificationservice.entity;

import com.caerus.notificationservice.enums.Channel;
import com.caerus.notificationservice.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;
    
    @Column(nullable = false)
    private String recipient; // email, phone number, etc.
    @Column(columnDefinition = "text")
    private String subject;

    @Column(columnDefinition = "text")
    private String content;

    private int retries = 0;

    private Instant createdAt = Instant.now();
    private Instant sentAt;

    
}
