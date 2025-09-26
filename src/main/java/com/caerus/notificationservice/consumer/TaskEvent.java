package com.caerus.notificationservice.consumer;

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
