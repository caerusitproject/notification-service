package com.caerus.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender implements Sender {

    @Autowired(required = false)
    private JavaMailSender mailSender;
      


	@Override
	public void send(String to, String subject, String msg) throws Exception {
		SimpleMailMessage message = new SimpleMailMessage(); 
        //message.setFrom();
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(msg);
        mailSender.send(message);
		
	}
}
