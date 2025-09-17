package com.caerus.notificationservice.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class TemplateProcessor {
	
	
	 private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(.*?)\\}\\}");

	 /**
	     * Process template by replacing placeholders with actual values.
	     *
	     * @param template Template string with placeholders like {{name}}
	     * @param values   Key-value map of placeholders
	     * @return Processed template
	     */
	    public String processTemplate(String template, Map<String, Object> values) {
	        if (template == null || template.isEmpty()) {
	            return "";
	        }

	        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
	        StringBuffer result = new StringBuffer();

	        while (matcher.find()) {
	            String key = matcher.group(1).trim();
	            Object value = values.getOrDefault(key, "");
	            matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
	        }
	        matcher.appendTail(result);

	        return result.toString();
	    }
}