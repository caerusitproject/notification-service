package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;

public interface NotificationService {
    String sendNotificationAndGetHtml(NotificationEvent event);
}
