package com.bevis.email;

import com.amazonaws.services.simpleemail.model.RawMessage;
import com.bevis.email.dto.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
class RawMessageBuilderImpl implements RawMessageBuilder {

    @Override
    public RawMessage buildRawMessage(Email email) {
        try {
            Properties properties = System.getProperties();
            Session defaultInstance = Session.getDefaultInstance(properties);
            MimeMessage mimeMessage = new MimeMessage(defaultInstance);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            String[] to = mapReceivers(email);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(new InternetAddress(email.getSenderAddress(), email.getSenderAddress()));
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getBody(), email.isHtml());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            if (Objects.nonNull(email.getAttachments()) && !email.getAttachments().isEmpty()) {
                for (File attachment : email.getAttachments()) {
                    mimeMessageHelper.addAttachment(attachment.getName(), attachment);
                }
            }
            mimeMessage.writeTo(outputStream);
            return new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

        } catch (Exception e){
            throw new EmailException("Error constructing RawMessage", e);
        }
    }

    private String[] mapReceivers(Email email) {
        Set<String> receiverEmails = new HashSet<>();
        if (Objects.nonNull(email.getReceiverAddresses())) {
            receiverEmails.addAll(email.getReceiverAddresses());
        }
        if (Objects.nonNull(email.getReceiverAddress())) {
            receiverEmails.add(email.getReceiverAddress());
        }
        return receiverEmails.toArray(new String[0]);
    }
}
