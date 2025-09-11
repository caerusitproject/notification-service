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
import com.caerus.notificationservice.model.Status;
import com.caerus.notificationservice.model.UserPreference;
import com.caerus.notificationservice.repo.NotificationRepository;
import com.caerus.notificationservice.repo.UserPreferenceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotificationService {
	
	@Autowired
	private RestTemplate restTemplate;

    private final NotificationRepository notificationRepository;
    private final UserPreferenceRepository preferenceRepository;
    private final EmailSender emailSender;
    private final SmsSender smsSender;
    private final WhatsAppSender whatsAppSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationService(NotificationRepository notificationRepository,
                               UserPreferenceRepository preferenceRepository,
                               EmailSender emailSender,
                               SmsSender smsSender,
                               WhatsAppSender whatsAppSender) {
        this.notificationRepository = notificationRepository;
        this.preferenceRepository = preferenceRepository;
        this.emailSender = emailSender;
        this.smsSender = smsSender;
        this.whatsAppSender = whatsAppSender;
    }

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
            prefOpt = preferenceRepository.findById(req.getUserId());
           
        } catch (Exception e) {
            // ignore parsing/lookup issues; proceed with available info
        }
        String email = prefOpt.map(UserPreference::getEmail).orElse(null);
        String phone = prefOpt.map(UserPreference::getPhone).orElse(null);
        userNotification.setRecipient(email != null ? email : phone != null ? phone : "unknown");

        try {
            switch (req.getChannel()) {
                case EMAIL -> {
                    if (email == null) throw new IllegalStateException("Email missing");
                    //emailSender.send(email, req.getContent());
                    System.out.println("Sending email to: " + email);
                }
                case SMS -> {
                    if (phone == null) throw new IllegalStateException("Phone missing");
                    smsSender.send(phone, req.getContent());
                }
                case WHATSAPP -> {
                    if (phone == null) throw new IllegalStateException("Phone missing");
                    whatsAppSender.send(phone, req.getContent());
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }
    
    //Read data from kafka and save to DB
    public Notification  processMessage(String message) {
    	Notification userNotification = null;
    	Optional<UserPreference> prefOpt = Optional.empty();
    	
        try {
            // Convert JSON → Object
        	NotificationMessage notification = objectMapper.readValue(message, NotificationMessage.class);
			System.out.println("✅ Parsed Kafka message: " + notification);	
			SendRequest sendRequest = new SendRequest();
			sendRequest.setSubject(notification.getSubject());
			sendRequest.setContent(notification.getMessage());
			sendRequest.setChannel(Channel.EMAIL);
			sendRequest.setUserId(notification.getUserId());
			prefOpt = preferenceRepository.findById(sendRequest.getUserId());
			if(!prefOpt.isPresent()) {
				
				UserPreference userPreference = new UserPreference();
				userPreference.setUserId(sendRequest.getUserId());
				userPreference.setEmail(notification.getEmail());
				userPreference.setPhone(notification.getPhoneNumber());
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
