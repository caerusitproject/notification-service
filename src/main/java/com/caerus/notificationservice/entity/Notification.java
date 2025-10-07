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
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    @Column(nullable = false)
    private String recipient;

    private String subject;


    @Column(columnDefinition = "text")
    private String content;

    private int retries;
    private Instant createdAt;
    private Instant sentAt;
    
}
