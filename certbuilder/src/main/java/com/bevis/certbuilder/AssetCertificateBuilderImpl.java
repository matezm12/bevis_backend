package com.bevis.certbuilder;

import com.bevis.certbuilder.dto.AssetCertificateContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@Service
@RequiredArgsConstructor
@Slf4j
class AssetCertificateBuilderImpl implements AssetCertificateBuilder {

    private final SpringTemplateEngine templateEngine;
    private final CertificateBuilderProps certificateBuilderProps;

    @Override
    public void constructCertificate(AssetCertificateContext context, OutputStream outputStream) throws IOException {
        String html = generateHtmlFromTemplate(context);
        log.trace(html);
        outputStream.write(html.getBytes(Charset.defaultCharset()));
        log.debug("constructCertificate Succeed");
    }

    private String generateHtmlFromTemplate(Object data) {
        Context context = new Context();
        context.setVariable("data", data);
        return templateEngine.process(certificateBuilderProps.getTemplateName(), context);
    }
}
