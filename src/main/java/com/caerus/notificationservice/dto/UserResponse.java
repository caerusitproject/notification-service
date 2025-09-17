package com.caerus.notificationservice.dto;

public record UserResponse(
        Long id,
        String name,
        String username,
        String email,
        String phoneNumber
) {}