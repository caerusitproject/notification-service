package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;
import com.caerus.notificationservice.entity.Notification;
import com.caerus.notificationservice.enums.Channel;
import com.caerus.notificationservice.enums.Status;
import com.caerus.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationOrchestratorService {

    private final EmailNotificationService emailService;
    private final NotificationRepository notificationRepository;

    public void processEvent(NotificationEvent event) {
       if(event.email()!=null){
           persistAndSend(event, Channel.EMAIL, emailService);
       }
    }

    private void persistAndSend(NotificationEvent event, Channel channel, NotificationService channelService) {
        Notification notification = Notification.builder()
                .userId(event.userId())
                .channel(channel)
                .recipient(getRecipient(event, channel))
                .subject("")
                .content("")
                .status(Status.PENDING)
                .createdAt(Instant.now())
                .retries(0)
                .build();

        notificationRepository.save(notification);

        try {
            String content = channelService.sendNotificationAndGetHtml(event);
            notification.setContent(content);

            if (channel == Channel.EMAIL) {
                notification.setSubject(((EmailNotificationService) channelService)
                        .resolveSubject(event.eventType()));
            }

            notification.setStatus(Status.SENT);
            notification.setSentAt(Instant.now());
            notificationRepository.save(notification);

        } catch (Exception e) {
            notification.setStatus(Status.FAILED);
            notification.setRetries(notification.getRetries() + 1);
            notificationRepository.save(notification);
            log.error("Failed to send {} notification for user {}: {}", channel, notification.getRecipient(), e.getMessage());
        }
    }


    private String getRecipient(NotificationEvent event, Channel channel){
        return switch (channel){
            case EMAIL -> event.email();
            case SMS -> event.phoneNumber();
            case WHATSAPP -> event.whatsappNumber();
            case IN_APP -> event.userId().toString();
        };
    }
}
