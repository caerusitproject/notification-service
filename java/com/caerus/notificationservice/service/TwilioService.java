package com.caerus.notificationservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.accountSid:}")
    private String accountSid;

    @Value("${twilio.authToken:}")
    private String authToken;

    @Value("${twilio.fromNumber:}")
    private String fromNumber;

    @Value("${twilio.whatsappFrom:}")
    private String whatsappFrom; // e.g., whatsapp:+1415...

    @PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.isBlank()) {
            Twilio.init(accountSid, authToken);
        }
    }

    public Message sendSms(String to, String body) {
        return Message.creator(new PhoneNumber(to), new PhoneNumber(fromNumber), body).create();
    }

    public Message sendWhatsApp(String to, String body) {
        // to must be like "whatsapp:+{number}"
        return Message.creator(new PhoneNumber("whatsapp:" + to), new PhoneNumber(whatsappFrom), body).create();
    }
}
