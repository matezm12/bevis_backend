package com.bevis.certificatecore.impl;

import com.bevis.certificatecore.EmailCertAttachmentBuilder;
import com.bevis.certificatecore.exception.CertificateException;
import com.bevis.pdf.PdfRenderer;
import com.bevis.resources.ResourcePathLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.bevis.certificatecore.impl.CertificateConstants.PDF_CERTIFICATE_TEMPLATE;

@Service
@Slf4j
@RequiredArgsConstructor
class PdfCertificateFileBuilderImpl implements EmailCertAttachmentBuilder {

    private final SpringTemplateEngine templateEngine;
    private final PdfRenderer pdfRenderer;
    private final CertificateProps certificateProps;
    private final ResourcePathLoader resourcePathLoader;

    @Override
    public File build(Object certificateData) {
        try {
            String renderedHtmlContent = generateCertificateHtml(certificateData);
            log.debug("Certificate html generated.");
            log.trace(renderedHtmlContent);
            File certificate = renderFile(renderedHtmlContent);
            log.debug("Certificate PDF file rendered.");
            return certificate;
        } catch (IOException e){
            log.error(e.getMessage());
            throw new CertificateException("Error building certificate: " + e.getMessage(), e);
        }
    }

    private String generateCertificateHtml(Object value) {
        Context context = new Context();
        context.setVariable("data", value);
        return templateEngine.process(PDF_CERTIFICATE_TEMPLATE, context);
    }

    private File renderFile(String renderedHtmlContent) throws IOException {
        File certificate = File.createTempFile("certificate", ".pdf");
        try (OutputStream fileStream = new FileOutputStream(certificate)){
            String resourcesFolder = getAbsoluteResourcesFolder();
            log.debug("Certificate resources folder: {}", resourcesFolder);
            pdfRenderer.renderHtmlToPdf(renderedHtmlContent, resourcesFolder, fileStream);
        }
        return certificate;
    }

    private String getAbsoluteResourcesFolder() {
        return resourcePathLoader.getResourceFolder(certificateProps.getResourcesFolder());
    }

}
