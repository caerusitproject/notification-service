package com.caerus.notificationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.caerus.notificationservice.entity.NotificationTemplate;
@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

   // Optional<NotificationTemplate> findByNameAndType(String fullName, String type);
   // Optional<NotificationTemplate> findByNotificationType(String nnotificationTypeame);
	  Optional<NotificationTemplate> findByTemplateNameAndType(String templateName, String type);
	 
    
    boolean existsByTemplateName(String name);
}