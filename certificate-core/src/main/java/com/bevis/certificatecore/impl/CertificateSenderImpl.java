package com.bevis.certificatecore.impl;

import com.bevis.certificatecore.CertificateSender;
import com.bevis.email.EmailSenderService;
import com.bevis.email.dto.Email;
import com.bevis.email.dto.EmailResult;
import com.bevis.emailcore.EmailBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.Objects;

import static com.bevis.emailcore.EmailTemplateConstants.CERTIFICATE_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
class CertificateSenderImpl implements CertificateSender {

    @Value("${sender.email}")
    private String emailSender;

    private final EmailSenderService emailSenderService;
    private final EmailBuilder emailBuilder;

    @Override
    public void send(String receiverEmail, Object certificateData, File file) {
        String html = emailBuilder.buildHtml(CERTIFICATE_EMAIL, certificateData);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddress(receiverEmail)
                .subject("Certificate")
                .body(html)
                .html(true)
                .build();
        if (Objects.nonNull(file)) {
            email.setAttachments(Collections.singletonList(file));
        }
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }
}
