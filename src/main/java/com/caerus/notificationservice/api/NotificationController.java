package com.caerus.notificationservice.api;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.caerus.notificationservice.dto.SendRequest;
import com.caerus.notificationservice.model.Notification;
import com.caerus.notificationservice.repo.NotificationRepository;
import com.caerus.notificationservice.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	@Autowired
	RestTemplate restTemplate;

    private final NotificationService service;
    private final NotificationRepository repo;

    public NotificationController(NotificationService service, NotificationRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/send")
    public Notification send(@Valid @RequestBody SendRequest req) {
        return service.send(req);
    }

    @GetMapping
    public List<Notification> all() {
        return repo.findAll();
    }
}
