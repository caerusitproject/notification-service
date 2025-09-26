package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;
import com.caerus.notificationservice.exception.NotificationDeliveryException;
import com.caerus.notificationservice.template.EmailTemplateProcessor;
import com.caerus.notificationservice.template.EmailTemplateType;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService{

    private final JavaMailSender mailSender;
    private final EmailTemplateProcessor templateProcessor;

    @Override
    public void sendNotification(NotificationEvent event) {
        try {
            EmailTemplateType type = resolveTemplate(event.eventType());
            String content = templateProcessor.buildContent(
                    type,
                    Map.of(
                            "name", event.fullName(),
                            "email", event.email(),
                           // "resetLink", event.getResetLink(),  // only used for forgot password
                            "title", "Notification"
                           // "message", event.getMessage()
                    )
            );

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(event.email());
            helper.setSubject("Notification: " + event.eventType());
            helper.setText(content, true); // HTML enabled

            mailSender.send(message);
            log.info("Email sent to {}", event.email());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", event.email(), e.getMessage(), e);
            throw new NotificationDeliveryException("Failed to send email: "+e.getMessage());
        }
    }

    private EmailTemplateType resolveTemplate(String eventType) {
        return switch (eventType) {
            case "USER_REGISTERED" -> EmailTemplateType.USER_REGISTRATION;
            case "FORGOT_PASSWORD" -> EmailTemplateType.FORGOT_PASSWORD;
            default -> EmailTemplateType.GENERIC_MESSAGE;
        };
    }
}
