package com.caerus.notificationservice.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageTemplateProcessor {

    public String buildMessage(String eventType, String name, String resetLink) {
        return switch (eventType) {
            case "USER_REGISTERED" ->
                    String.format("Welcome %s! Your registration was successful. Enjoy using Track It!", name);
            case "FORGOT_PASSWORD" ->
                    String.format("Hi %s, click the link to reset your password: %s", name, resetLink);
            default ->
                    String.format("Hi %s, you have a new notification from Track It!", name);
        };
    }
}