package com.caerus.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caerus.notificationservice.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
