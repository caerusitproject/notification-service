package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;
import com.caerus.notificationservice.exception.NotificationDeliveryException;
import com.caerus.notificationservice.template.MessageTemplateProcessor;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsNotificationService implements NotificationService{

    @Value("${twilio.phone-number}")
    private String fromNumber;

    private final MessageTemplateProcessor messageTemplateProcessor;

    @Override
    public String sendNotificationAndGetContent(NotificationEvent event) {
        try{
            String body = messageTemplateProcessor.buildMessage(
                    event.eventType(),
                    event.fullName(),
                    event.resetLink()
            );

            Message message = Message.creator(
                    new PhoneNumber(event.phoneNumber()),
                    new PhoneNumber(fromNumber),
                    body
            ).create();

            log.info("SMS sent successfully to {}. SID: {}", event.phoneNumber(), message.getSid());
            return body;

        }catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", event.phoneNumber(), e.getMessage());
            throw new NotificationDeliveryException("Failed to send SMS: " + e.getMessage());
        }
    }
}
