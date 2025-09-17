package com.caerus.notificationservice.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caerus.notificationservice.model.Channel;
import com.caerus.notificationservice.model.NotificationTemplate;
import com.caerus.notificationservice.repo.NotificationTemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationTemplateService.class);

    private final NotificationTemplateRepository templateRepository;
    
    @Value("${file.upload-dir}")
    private String TEMPLATE_FOLDER;

	/*
	 * public NotificationTemplate saveTemplate(NotificationTemplate template) {
	 * return templateRepository.save(template); }
	 */
    
    public NotificationTemplate saveTemplate(NotificationTemplate template) {
        if (templateRepository.existsByTemplateName(template.getTemplateName())) {
			throw new IllegalArgumentException("Template with name " + template.getTemplateName() + " already exists.");
		}

        NotificationTemplate nTemplate = NotificationTemplate.builder()
        		.templateName(template.getTemplateName())
				.type(template.getType())
				.notificationType(template.getNotificationType())
				.EventType(template.getEventType())
				.subject(template.getSubject())
				.content(template.getContent())
				.build();

        return templateRepository.save(template);
    }


    
    public Optional<NotificationTemplate> getTemplateById(Long id) {
		return templateRepository.findById(id);
	}
    
    

    public List<NotificationTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
    
    public String processTemplate(String template, Map<String, Object> values) {
        String result = template;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            result = result.replace(placeholder, entry.getValue().toString());
        }
        return result;
    }


	public NotificationTemplate getTemplate(String templateName, String templateType) {
		 		
		return templateRepository.findByTemplateNameAndType(templateName, templateType)
				.orElseThrow(() -> new IllegalArgumentException(
						"Template not found with name: " + templateName + " and type: " + templateType));
	}
	
	public NotificationTemplate getTemplateByEventType(String eventtype, Channel channel) {
 		
		return templateRepository.findAll().stream().filter(
				t -> t.getEventType().equalsIgnoreCase(eventtype) && t.getType().equalsIgnoreCase(channel.name()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException(
						"Template not found with event type: " + eventtype + " and channel: " + channel));
	}
	
	//uplode template file and save to db 
	//TODO
	public NotificationTemplate uploadTemplateFile(String name, String description, MultipartFile file) throws Exception {
		if (templateRepository.existsByTemplateName(name)) {
            throw new RuntimeException("Template with this name already exists!");
        }
        // read file content
		File folder = new File(TEMPLATE_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        
        // Save file in resources/email-template
        String filePath = TEMPLATE_FOLDER + file.getOriginalFilename();
        File savedFile = new File(filePath);
        file.transferTo(savedFile);

        // Save record in DB
        NotificationTemplate template = NotificationTemplate.builder()
                .templateName(name)
                .filePath(filePath)
                .description(description)
                .build();

		return templateRepository.save(template);
		

	}
}