package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;
import com.caerus.notificationservice.enums.EmailTemplateType;
import com.caerus.notificationservice.exception.NotificationDeliveryException;
import com.caerus.notificationservice.template.EmailTemplateProcessor;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationService{

    private final JavaMailSender mailSender;
    private final EmailTemplateProcessor templateProcessor;

    @Override
    public String sendNotificationAndGetHtml(NotificationEvent event) {
        try {
            EmailTemplateType type = resolveTemplate(event.eventType());
            Map<String, Object> variables = buildTemplateVariables(event, type);

            String content = templateProcessor.buildContent(type, variables);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(event.email());
            helper.setSubject(resolveSubject(event.eventType()));
            helper.setText(content, true);

            mailSender.send(message);
            log.info("Email sent to {}", event.email());

            return content;
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

    String resolveSubject(String eventType) {
        return switch (eventType) {
            case "USER_REGISTERED" -> "Welcome to Track It!";
            case "FORGOT_PASSWORD" -> "Reset your password";
            default -> "Notification from Track It";
        };
    }

    private Map<String, Object> buildTemplateVariables(NotificationEvent event, EmailTemplateType type){
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", event.fullName());
        variables.put("email", event.email());
        variables.put("title", resolveSubject(event.eventType()));

        if (type == EmailTemplateType.FORGOT_PASSWORD) {
            variables.put("resetLink", event.resetLink());
        }
        return variables;
    }
}
