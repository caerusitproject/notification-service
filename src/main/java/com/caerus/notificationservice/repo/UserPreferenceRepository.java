package com.caerus.notificationservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caerus.notificationservice.model.UserPreference;

import java.util.Optional;
import java.util.UUID;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
	
	    UserPreference findByUserIdAndNotificationType(Long userId, String notificationType);
	    UserPreference findByUserId(Long userId);
	    Optional<UserPreference> findByEmail(String email);
}
