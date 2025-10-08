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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationOrchestratorService {

    private final EmailNotificationService emailService;
    private final SmsNotificationService smsService;
    private final WhatsappNotificationService whatsappService;
    private final NotificationRepository notificationRepository;

    public void processEvent(NotificationEvent event) {
        if(event.channels()==null || event.channels().isEmpty()){
            log.info("No channels provided for user {}", event.userId());
            return;
        }

        event.channels().forEach(channelName -> {
            try{
                Channel channel = Channel.valueOf(channelName.toUpperCase());
                NotificationService service = resolveService(channel);

                persistAndSend(event, channel, service);
            } catch (IllegalArgumentException ex){
                log.error("Unsupported channel '{}' for user {}", channelName, event.userId());
            } catch (Exception e) {
                log.error("Error while processing channel {} for user {}: {}", channelName, event.userId(), e.getMessage(), e);
            }
        });
    }

    private NotificationService resolveService(Channel channel) {
        return switch (channel) {
            case EMAIL -> emailService;
            case SMS -> smsService;
            case WHATSAPP -> whatsappService;
            case IN_APP -> throw new UnsupportedOperationException("IN_APP notifications not yet supported");
        };
    }

    private void persistAndSend(NotificationEvent event, Channel channel, NotificationService notificationService) {
        Notification notification = Notification.builder()
                .userId(event.userId())
                .fullName(event.fullName())
                .channel(channel)
                .recipient(getRecipient(event, channel))
                .subject("")
                .content("")
                .resetLink(event.resetLink())
                .eventType(event.eventType())
                .status(Status.PENDING)
                .createdAt(Instant.now())
                .retries(0)
                .build();

        notificationRepository.save(notification);

        try {
            String content = notificationService.sendNotificationAndGetContent(event);
            notification.setContent(content);

            if (channel == Channel.EMAIL) {
                notification.setSubject(((EmailNotificationService) notificationService)
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

    public void retryNotification(Notification notification) {
        try {
            Channel channel = notification.getChannel();
            NotificationService service = resolveService(channel);

            NotificationEvent event = new NotificationEvent(
                    notification.getUserId(),
                    notification.getFullName(),
                    notification.getEventType(),
                    channel == Channel.EMAIL ? notification.getRecipient() : null,
                    notification.getResetLink(),
                    channel == Channel.SMS ? notification.getRecipient() : null,
                    channel == Channel.WHATSAPP ? notification.getRecipient() : null,
                    List.of(channel.name())
            );

            String content = service.sendNotificationAndGetContent(event);
            notification.setContent(content);
            notification.setStatus(Status.SENT);
            notification.setSentAt(Instant.now());
            log.info("Retried successfully for notification ID {}", notification.getId());
        } catch (Exception e) {
            notification.setRetries(notification.getRetries() + 1);
            notification.setStatus(Status.FAILED);
            log.error("Retry failed for notification ID {}: {}", notification.getId(), e.getMessage());
        }

        notificationRepository.save(notification);
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
