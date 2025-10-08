package com.caerus.notificationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka.topic")
public class KafkaTopicsProperties {
    private String notificationEvents;
    private String notificationEventsDlq;
}
