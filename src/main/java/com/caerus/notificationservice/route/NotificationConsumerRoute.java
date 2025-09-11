package com.caerus.notificationservice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumerRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("kafka:user.notification?brokers=localhost:29092&groupId=notification-service")
            .routeId("notificationConsumerRoute")
            .log("📩 Raw message from Kafka: ${body}")
            .bean("notificationService", "processMessage");
    }
}