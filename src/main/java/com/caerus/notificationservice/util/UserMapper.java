package com.caerus.notificationservice.util;

import com.caerus.notificationservice.dto.UserEventDTO;
import com.caerus.notificationservice.entity.UserPreference;

public class UserMapper {

    public static UserPreference toEntity(UserEventDTO dto) {
        if (dto == null) return null;

        UserPreference user = new UserPreference();
        user.setUserId(dto.getUserId());
        user.setNotificationType(dto.getEventType());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhoneNumber());
        user.setWhatsappNumber(dto.getPhoneNumber());
        return user;
    }
}