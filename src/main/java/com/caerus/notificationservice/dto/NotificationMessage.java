package com.caerus.notificationservice.dto;
public class NotificationMessage {
    private Long userId;
    private String email;
    private String subject;
    private String message;
    private String phoneNumber;
    private String whatsappNumber;

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getWhatsappNumber() { return whatsappNumber; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", whatsappNumber='" + whatsappNumber + '\'' +
                '}';
    }
}