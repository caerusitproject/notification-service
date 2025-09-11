package com.caerus.notificationservice.service;
public interface Sender {
    void send(String to, String message) throws Exception;
}
