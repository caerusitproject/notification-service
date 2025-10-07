package com.caerus.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.kafka.topic")
public class KafkaTopicsProperties {
    private String notificationEvents;

    public String getNotificationEvents() {
        return notificationEvents;
    }

    public void setNotificationEvents(String notificationEvents) {
        this.notificationEvents = notificationEvents;
    }
}
