package com.caerus.notificationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caerus.notificationservice.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
