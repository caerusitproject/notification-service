package com.caerus.notificationservice.service;

import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppSender implements Sender {

    private final TwilioService twilioService;

    public WhatsAppSender(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @Override
    public void send(String to, String message) throws Exception {
        try {
            Message m = twilioService.sendWhatsApp(to, message);
            System.out.println("[WhatsApp] sid=" + m.getSid());
        } catch (ApiException e) {
            System.err.println("Twilio API error: " + e.getMessage());
            throw e;
        }
    }
}
