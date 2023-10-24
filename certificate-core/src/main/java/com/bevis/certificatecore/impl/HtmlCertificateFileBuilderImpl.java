package com.bevis.certificatecore.impl;

import com.bevis.certificatecore.CertificateFileBuilder;
import com.bevis.certificatecore.exception.CertificateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static com.bevis.certificatecore.impl.CertificateConstants.HTML_CERTIFICATE_TEMPLATE;


@Primary
@Service
@Slf4j
@RequiredArgsConstructor
class HtmlCertificateFileBuilderImpl implements CertificateFileBuilder {

    private final SpringTemplateEngine templateEngine;

    @Override
    public File build(Object certificateData) {
        return build(certificateData, HTML_CERTIFICATE_TEMPLATE);
    }

    private File build(Object certificateData, String template) {
        try {
            File certificate = File.createTempFile("certificate", ".htm");
            String renderedHtmlContent = generateCertificateHtml(certificateData, template);
            FileUtils.writeStringToFile(certificate, renderedHtmlContent, Charset.defaultCharset());
            return certificate;
        } catch (IOException e){
            log.error(e.getMessage());
            throw new CertificateException("Error building certificate: " + e.getMessage(), e);
        }
    }

    private String generateCertificateHtml(Object value, String template) {
        Context context = new Context();
        context.setVariable("data", value);
        return templateEngine.process(template, context);
    }
}
