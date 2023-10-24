package com.bevis.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import com.bevis.email.dto.Email;
import com.bevis.email.dto.EmailResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
//@Service
@RequiredArgsConstructor
class AwsEmailSenderServiceImpl implements EmailSenderService {

    private final AmazonSimpleEmailService emailService;
    private final RawMessageBuilder rawMessageBuilder;

    @Override
    public EmailResult send(Email email) {
        log.debug("Processing sending email...");
        RawMessage rawMessage = rawMessageBuilder.buildRawMessage(email);
        SendRawEmailResult sendEmailResult = emailService.sendRawEmail(new SendRawEmailRequest(rawMessage));
        log.info("Message sent successfully to Email: {} with message id {}", email.getReceiverAddress(), sendEmailResult.getMessageId());
        log.debug("Message body: {}", email);
        return EmailResult.builder()
                .params(Collections.singletonMap("messageId", sendEmailResult.getMessageId()))
                .build();
    }

}
