package com.caerus.notificationservice.scheduler;

import com.caerus.notificationservice.entity.Notification;
import com.caerus.notificationservice.enums.Status;
import com.caerus.notificationservice.repository.NotificationRepository;
import com.caerus.notificationservice.service.NotificationOrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRetryScheduler {
    private final NotificationRepository notificationRepository;
    private final NotificationOrchestratorService orchestratorService;

    /**
     * Retry every 5 minutes for failed notifications.
     */
    @Scheduled(fixedRate = 300000)
    public void retryFailedNotifications() {
        List<Notification> failedNotifications = notificationRepository.findTop20ByStatusOrderByCreatedAtAsc(Status.FAILED);

        if (failedNotifications.isEmpty()) {
            return;
        }

        log.info("Retrying {} failed notifications", failedNotifications.size());

        for (Notification notification : failedNotifications) {
            try {
                orchestratorService.retryNotification(notification);
            } catch (Exception e) {
                log.error("Retry failed for notification ID {}: {}", notification.getId(), e.getMessage(), e);
            }
        }
    }
}
