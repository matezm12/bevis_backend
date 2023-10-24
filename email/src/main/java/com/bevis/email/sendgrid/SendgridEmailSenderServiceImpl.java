package com.bevis.email.sendgrid;

import com.bevis.email.EmailException;
import com.bevis.email.EmailSenderService;
import com.bevis.email.dto.Email;
import com.bevis.email.dto.EmailResult;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import static com.bevis.email.sendgrid.SendGridUtil.convertFileToAttachment;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendgridEmailSenderServiceImpl implements EmailSenderService {

    private final SendGridProps sendGridProps;

    @Override
    public EmailResult send(Email email) {
        com.sendgrid.helpers.mail.objects.Email from = new com.sendgrid.helpers.mail.objects.Email(email.getSenderAddress());
        String subject = email.getSubject();
        com.sendgrid.helpers.mail.objects.Email to = new com.sendgrid.helpers.mail.objects.Email(email.getReceiverAddress());

        Content content;
        if (email.isHtml()) {
            content = new Content("text/html", email.getBody());
        } else {
            content = new Content("text/plain", email.getBody());
        }

        try {
            Mail mail = new Mail(from, subject, to, content);

            if (Objects.nonNull(email.getReceiverAddresses()) && !email.getReceiverAddresses().isEmpty()) {
                for (String receiverAddress : email.getReceiverAddresses()) {
                    Personalization personalization = new Personalization();
                    personalization.addTo(new com.sendgrid.helpers.mail.objects.Email(receiverAddress));
                    mail.addPersonalization(personalization);
                }
            }

            if (Objects.nonNull(email.getAttachments()) && !email.getAttachments().isEmpty()) {
                for (File file : email.getAttachments()) {
                    mail.addAttachments(convertFileToAttachment(file));
                }
            }

            SendGrid sendGrid = new SendGrid(sendGridProps.getApiKey());

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            log.debug("Status code: {}", response.getStatusCode());
            log.debug(response.getBody());
            log.debug("Headers: {}", response.getHeaders());

            return EmailResult.builder()
                    .params(Collections.singletonMap("messageId", response.getHeaders().get("X-Message-Id")))
                    .build();
        } catch (IOException e) {
            throw new EmailException("Error sending email", e);
        }
    }
}
