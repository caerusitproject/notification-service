package com.caerus.notificationservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caerus.notificationservice.entity.NotificationTemplate;
import com.caerus.notificationservice.service.NotificationTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notification-templates")
@RequiredArgsConstructor
public class NotificationTemplateConrtroller {
	
	private final NotificationTemplateService templateService;

    // Create a new template
    @PostMapping("/create-template")
    public ResponseEntity<NotificationTemplate> createTemplate(@RequestBody NotificationTemplate template) {
        NotificationTemplate savedTemplate = templateService.saveTemplate(template);
        return ResponseEntity.ok(savedTemplate);
    }
    
    @PostMapping("/upload-template")
    public ResponseEntity<NotificationTemplate> uploadTemplate(
    		@RequestParam("file") MultipartFile file,
            @RequestParam("templateName") String templateName,
            @RequestParam("type") String type,
            @RequestParam("notificationType") String notificationType,
            @RequestParam("subject") String subject,
            @RequestParam("eventType") String eventType) throws IOException {

        try {
        	NotificationTemplate savedTemplate = templateService.uploadTemplateFile(templateName,type,notificationType,subject,eventType, file);
            return ResponseEntity.ok(savedTemplate);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all templates
    @GetMapping
    public ResponseEntity<List<NotificationTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    // Get template by fullName and type
       @GetMapping("/{fullName}/{type}")
       public ResponseEntity<NotificationTemplate> getTemplateByNameAndType(
            @PathVariable String name,
            @PathVariable String type) {
        Optional<NotificationTemplate> templateOpt = templateService.getAllTemplates()
                .stream()
                .filter(t -> t.getTemplateName().equals(name) && t.getType().equals(type))
                .findFirst();

        return ResponseEntity.ok(templateOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()).getBody());
       }
    
    // Get template by fullName and type
    @GetMapping("/{notificationType}")
    public ResponseEntity<NotificationTemplate> getTemplateByNotificationType(
            @PathVariable String NotificationType) {
		Optional<NotificationTemplate> templateOpt = templateService.getAllTemplates().stream()
				.filter(t -> t.getNotificationType().equals(NotificationType)).findFirst();
				return templateOpt.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());

       
    }

    // Update template
    @PutMapping("/{id}")
    public ResponseEntity<NotificationTemplate> updateTemplate(
            @PathVariable Long id,
            @RequestBody NotificationTemplate template) {

        Optional<NotificationTemplate> existingTemplate = templateService.getAllTemplates()
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        if (existingTemplate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        NotificationTemplate toUpdate = existingTemplate.get();
        toUpdate.setTemplateName(template.getTemplateName());
        toUpdate.setType(template.getType());
        toUpdate.setNotificationType(template.getNotificationType());
        toUpdate.setEventType(template.getEventType());
        toUpdate.setContent(template.getContent());
        toUpdate.setDescription(template.getDescription());

        return ResponseEntity.ok(templateService.saveTemplate(toUpdate));
    }

    // Delete template
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok("Template deleted successfully!");
    }

}
