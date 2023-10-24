package com.bevis.emailcore.impl;

import com.bevis.emailcore.EmailBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThymeleafEmailBuilderImpl implements EmailBuilder {

    private static final String DATA = "data";

    private final SpringTemplateEngine templateEngine;

    @Override
    public String buildHtml(String templateName, Object data) {
        Context context = new Context();
        context.setVariable(DATA, data);
        return templateEngine.process(templateName, context);
    }
}
