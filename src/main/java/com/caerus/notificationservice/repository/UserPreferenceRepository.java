package com.caerus.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caerus.notificationservice.entity.UserPreference;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
	
	    UserPreference findByUserIdAndNotificationType(Long userId, String notificationType);
	    UserPreference findByUserId(Long userId);
	    Optional<UserPreference> findByEmail(String email);
}
