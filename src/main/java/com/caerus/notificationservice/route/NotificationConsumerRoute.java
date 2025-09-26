package com.caerus.notificationservice.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class NotificationConsumerRoute { //extends RouteBuilder {
	
//	@Value("${app.kafka.topic.user-registered}")
//     private String topicName;
//	@Value("${spring.kafka.bootstrap-servers}")
//	private String kafkaBroker;
//
//    @Override
//    public void configure() {
//        from("kafka:"+topicName+"?brokers="+kafkaBroker+"&groupId=notification-service")
//            .routeId("notificationConsumerRoute")
//            .log("📩 Raw message from Kafka: ${body}")
//            .bean("notificationService", "processMessage");
//    }
}