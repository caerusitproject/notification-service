package com.caerus.notificationservice.util;

import org.apache.camel.spi.annotations.Component;
import org.springframework.beans.factory.annotation.Value;


public class NotificationConstent {

	public static String EMAIL = "EMAIL";
	public static String SMS = "SMS";
	public static String WHATSAPP = "WHATSAPP";

	public static String TEXT = "TEXT";
	public static String HTML = "HTML";

	public static String WELCOME = "WELCOME";
	public static String PASSWORD_RESET = "PASSWORD_RESET";

	public static String USER_REGISTRATION = "USER_REGISTRATION";
	public static String FORGOT_PASSWORD = "FORGOT_PASSWORD";

	@Value("${file.upload-dir}")
	public static String TEMPLATE_FOLDER;
}
