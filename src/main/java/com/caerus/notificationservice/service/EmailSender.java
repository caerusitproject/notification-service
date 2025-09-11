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
    public void send(String to, String message) throws Exception {
        if (mailSender == null) {
            throw new IllegalStateException("JavaMailSender not configured");
        }
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Notification");
        mail.setText(message);
        mailSender.send(mail);
    }
}
