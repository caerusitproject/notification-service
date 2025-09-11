package com.caerus.notificationservice.kafka;

import com.caerus.notificationservice.dto.SendRequest;
import com.caerus.notificationservice.model.Channel;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskEvent {
    public Long userId;
    public String subject;
    public String message;
    public String channel; // EMAIL/SMS/WHATSAPP/IN_APP
}
