package com.caerus.notificationservice.consumer;

import com.caerus.notificationservice.config.KafkaTopicsProperties;
import com.caerus.notificationservice.dto.NotificationEvent;
import com.caerus.notificationservice.service.NotificationOrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {

    private final NotificationOrchestratorService orchestratorService;
    private final KafkaTopicsProperties topics;

    @KafkaListener(topics = "#{@kafkaTopicsProperties.notificationEvents}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(NotificationEvent event){
        log.info("Received notification event: {}", event);
        try{
            orchestratorService.processEvent(event);
        } catch (Exception e) {
            log.error("Failed to process notification event: {}", e.getMessage(), e);
            // Optionally send to Dead Letter Queue here
        }
    }
}
