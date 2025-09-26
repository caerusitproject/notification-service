package com.caerus.notificationservice.consumer;

import com.caerus.notificationservice.config.KafkaTopicsProperties;
import com.caerus.notificationservice.dto.NotificationEvent;
import com.caerus.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationService notificationService;
    private final KafkaTopicsProperties topics;

    @KafkaListener(topics = "#{@kafkaTopicsProperties.userRegistered}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(NotificationEvent event){
        log.info("Received notification event: {}", event);
        notificationService.sendNotification(event);
    }
}
