package com.caerus.notificationservice.repository;

import com.caerus.notificationservice.entity.Notification;
import com.caerus.notificationservice.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByStatus(Status status);

  List<Notification> findTop20ByStatusOrderByCreatedAtAsc(Status status);
}
