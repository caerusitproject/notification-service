package com.caerus.notificationservice.service;

import com.caerus.notificationservice.dto.NotificationEvent;

public interface NotificationService {
    void sendNotification(NotificationEvent event);
}
