package com.caerus.notificationservice.template;

public enum EmailTemplateType {
    USER_REGISTRATION("user-registration"),
    FORGOT_PASSWORD("forgot-password"),
    GENERIC_MESSAGE("generic-message");

    private final String fileName;

    EmailTemplateType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
