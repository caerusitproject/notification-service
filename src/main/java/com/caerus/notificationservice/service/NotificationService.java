package com.caerus.notificationservice.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.caerus.notificationservice.dto.NotificationMessage;
import com.caerus.notificationservice.dto.SendRequest;
import com.caerus.notificationservice.model.Channel;
import com.caerus.notificationservice.model.Notification;
import com.caerus.notificationservice.model.NotificationTemplate;
import com.caerus.notificationservice.model.Status;
import com.caerus.notificationservice.model.UserPreference;
import com.caerus.notificationservice.processor.NotificationDataProvider;
import com.caerus.notificationservice.repo.NotificationRepository;
import com.caerus.notificationservice.repo.NotificationTemplateRepository;
import com.caerus.notificationservice.repo.UserPreferenceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	@Autowired
	private RestTemplate restTemplate;

    private final NotificationRepository notificationRepository;
    private final TemplateProcessor templateProcessor;
    private final UserPreferenceRepository preferenceRepository;
    private final EmailSender emailSender;
    private final SmsSender smsSender;
    private final WhatsAppSender whatsAppSender;
    private final NotificationTemplateService notificationtemplateService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationDataProvider dataProvider;

    

    @Transactional
    public Notification send(SendRequest req) {
        Notification userNotification = new Notification();
        userNotification.setUserId(req.getUserId());
       	userNotification.setStatus(Status.PENDING);
       	userNotification.setChannel(req.getChannel()!=null?req.getChannel():Channel.EMAIL);
       	userNotification.setSubject(req.getSubject());
       	userNotification.setContent(req.getContent());
       	
       	
       	Optional<UserPreference> prefOpt = Optional.empty();
        try {
            prefOpt = preferenceRepository.findByUserId(req.getUserId())!=null?Optional.of(preferenceRepository.findByUserId(req.getUserId())):Optional.empty();
           
        } catch (Exception e) {
            // ignore parsing/lookup issues; proceed with available info
        }
        String email = prefOpt.map(UserPreference::getEmail).orElse(null);
        String phone = prefOpt.map(UserPreference::getPhone).orElse(null);
        String NotificationType = prefOpt.map(UserPreference::getNotificationType).orElse(null);
        userNotification.setRecipient(email != null ? email : phone != null ? phone : "unknown");
        
        Map<String, Object> data =  new HashMap<>();
        data.put("userId", req.getUserId());
        data.put("email", email);
        data.put("phone", phone);
        data.put("username", email);
        
        
        
        NotificationTemplate emailTemplate = null; 

        //String messageContent = templateProcessor.processTemplate(emailTemplate.getContent(), req.getData());
        
       // userNotification.setContent(messageContent);
        
        try {
            switch (req.getChannel()) {
                case EMAIL -> {
                    if (email == null) throw new IllegalStateException("Email missing");
                    //
                    System.out.println("Sending email to: " + email);
                    emailTemplate = notificationtemplateService.getTemplateByEventType(NotificationType, Channel.EMAIL);
                    String messageContent = templateProcessor.processTemplate(emailTemplate.getContent(), data);
                    userNotification.setContent(messageContent);
                   
                    emailSender.send(email, emailTemplate.getSubject(),userNotification.getContent());
                }
                case SMS -> {
                    if (phone == null) throw new IllegalStateException("Phone missing");
                    smsSender.send(phone,null, req.getContent());
                }
                case WHATSAPP -> {
                    if (phone == null) throw new IllegalStateException("Phone missing");
                    whatsAppSender.send(phone,null, req.getContent());
                }
                case IN_APP -> {
                    // no-op placeholder
                    System.out.println("[InApp] " + req.getUserId() + " -> " + req.getContent());
                }
            }
            userNotification.setStatus(Status.SENT);
            userNotification.setSentAt(Instant.now());
            userNotification = notificationRepository.save(userNotification);
        } catch (Exception e) {
        	userNotification.setStatus(Status.FAILED);
        	userNotification.setRetries(userNotification.getRetries() + 1);
        }
        return userNotification;
    }
    
    
    

    public String sendMail(String to, String subject, String message) {
        String url = "http://localhost:8000/send-mail/";

        // Request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("to", to);
        requestBody.put("subject", subject);
        requestBody.put("message", message);

        try {
			emailSender.send(requestBody.get("to"), requestBody.get("subject"), requestBody.get("message"));
		} catch (Exception e) {
			System.err.println("❌ Failed to send email to: " + to);
			e.printStackTrace();
		}

        return "Mail send processed";
    }
    
    //Read data from kafka and save to DB
    public Notification  processMessage(String message) {
    	Notification userNotification = null;
    	Optional<UserPreference> prefOpt = Optional.empty();
    	
        try {
            // Convert JSON → Object
        	NotificationMessage notification = objectMapper.readValue(message, NotificationMessage.class); //fullname change
			System.out.println("✅ Parsed Kafka message: " + notification);	
			SendRequest sendRequest = new SendRequest();
			sendRequest.setChannel(notification.getChannel()!=null?Channel.valueOf(notification.getChannel()):Channel.EMAIL);
			sendRequest.setUserId(notification.getUserId());
			prefOpt = Optional.ofNullable(preferenceRepository.findByUserId(notification.getUserId()));
			if(!prefOpt.isPresent()) {
				
				UserPreference userPreference = new UserPreference();
				userPreference.setUserId(sendRequest.getUserId());
				userPreference.setEmail(notification.getEmail());
				userPreference.setPhone(notification.getPhoneNumber());
				userPreference.setNotificationType(notification.getEventType());
				userPreference.setWhatsappNumber(notification.getWhatsappNumber());
				
				if(userPreference.getEmail()!=null) {
					userPreference.setEmailEnabled(true);
				}
				if(userPreference.getPhone()!=null) {
					userPreference.setSmsEnabled(true);
				}
				if(userPreference.getWhatsappNumber()!=null) {
					userPreference.setWhatsappEnabled(true);
				}
								
				preferenceRepository.save(userPreference);
			}
			
			
			
			userNotification = send(sendRequest);
			System.out.println("✅ Notification processed and saved: " + userNotification);
            
        } catch (Exception e) {
            System.err.println("❌ Failed to parse Kafka message: " + message);
            e.printStackTrace();
        }
		return userNotification;
    }
}
