package com.caerus.notificationservice.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.caerus.notificationservice.dto.SendRequest;
import com.caerus.notificationservice.entity.Notification;
import com.caerus.notificationservice.repository.NotificationRepository;
import com.caerus.notificationservice.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;
    private final NotificationRepository repo;

    @PostMapping
    public Notification send(@Valid @RequestBody SendRequest req) {
        return service.send(req);
    }

    @GetMapping
    public List<Notification> all() {
        return repo.findAll();
    }
}
