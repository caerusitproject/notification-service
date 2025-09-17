package com.caerus.notificationservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.caerus.notificationservice.dto.SendRequest;
import com.caerus.notificationservice.dto.UserEventDTO;
import com.caerus.notificationservice.model.Channel;
import com.caerus.notificationservice.service.NotificationService;

@Component
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${app.kafka.topic.notification}", groupId = "${spring.application.name}")
    public void onMessage(TaskEvent event) {
        SendRequest req = new SendRequest();
        req.setUserId(event.userId);
        req.setSubject(event.subject);
        req.setContent(event.message);
        try {
            req.setChannel(Channel.valueOf(event.channel == null ? "IN_APP" : event.channel));
        } catch (Exception e) {
            req.setChannel(Channel.IN_APP);
        }
        notificationService.send(req);
    }
}
