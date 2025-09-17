package com.caerus.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSender implements Sender {

    @Autowired(required = false)
    private JavaMailSender mailSender;
      


	@Override
	public void send(String to, String subject, String msg) throws Exception {
		//SimpleMailMessage message = new SimpleMailMessage(); 
		MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to); 
        helper.setSubject(subject); 
        helper.setText(msg, true);
        mailSender.send(message);
		
	}
}
