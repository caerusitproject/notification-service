package com.caerus.notificationservice.service;
public interface Sender {
    void send(String to, String subject,  String message) throws Exception;
    //void sendEmail(String to,String subject,String body)throws Exception;
}
