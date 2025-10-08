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
public class WhatsappNotificationService implements NotificationService {

    @Value("${twilio.whatsapp-from}")
    private String whatsappFrom;

    private final MessageTemplateProcessor messageTemplateProcessor;

    @Override
    public String sendNotificationAndGetContent(NotificationEvent event) {
        try {
            String body = messageTemplateProcessor.buildMessage(
                    event.eventType(),
                    event.fullName(),
                    event.resetLink()
            );

            String from = "whatsapp:" + whatsappFrom;
            String to = "whatsapp:" + event.whatsappNumber();

            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body
            ).create();

            log.info("WhatsApp message sent to {}. SID: {}", event.whatsappNumber(), message.getSid());
            return body;

        } catch (Exception e) {
            log.error("Failed to send WhatsApp message to {}: {}", event.whatsappNumber(), e.getMessage());
            throw new NotificationDeliveryException("Failed to send WhatsApp message: " + e.getMessage());
        }
    }
}