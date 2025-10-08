package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;

public interface NotificationService {
    String sendNotificationAndGetContent(NotificationEvent event);
}
