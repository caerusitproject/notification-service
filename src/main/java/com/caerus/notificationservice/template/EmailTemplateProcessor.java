package com.caerus.notificationservice.template;

import com.caerus.notificationservice.enums.EmailTemplateType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailTemplateProcessor {
  private final TemplateEngine templateEngine;

  public String buildContent(EmailTemplateType type, Map<String, Object> variables) {
    Context context = new Context();
    context.setVariables(variables);
    return templateEngine.process(type.getFileName(), context);
  }
}
