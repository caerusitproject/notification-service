package com.caerus.notificationservice.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailTemplateProcessor {
    private final TemplateEngine templateEngine;

    public String buildContent(EmailTemplateType type, Map<String, Object> variables){
        Context context = new Context();
        context.setVariables(variables);
        return  templateEngine.process(type.getFileName(), context);
    }
}
