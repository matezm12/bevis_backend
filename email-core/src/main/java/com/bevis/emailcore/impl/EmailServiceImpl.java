package com.bevis.emailcore.impl;

import com.bevis.emailcore.EmailBuilder;
import com.bevis.emailcore.EmailService;
import com.bevis.emailcore.dto.PasswordChangedEmail;
import com.bevis.emailcore.dto.ResetPasswordEmail;
import com.bevis.emailcore.dto.VerificationEmail;
import com.bevis.assetimport.CodeReaderNotificationSender;
import com.bevis.user.domain.User;
import com.bevis.email.EmailSenderService;
import com.bevis.email.dto.Email;
import com.bevis.email.dto.EmailResult;
import com.bevis.user.dto.OnUserCreatedEvent;
import com.bevis.user.dto.OnUserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bevis.emailcore.EmailTemplateConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService, CodeReaderNotificationSender {

    @Value("${sender.email}")
    private String emailSender;

    @Value("${sender.activateBaseUrl}")
    private String activateBaseUrl;

    @Value("${sender.resetPasswordFinishBaseUrl}")
    private String resetPasswordFinishBaseUrl;

    private final EmailSenderService emailSenderService;
    private final EmailBuilder emailBuilder;

    @Async
    @Override
    public void sendVerificationEmail(VerificationEmail verificationEmail) {
        log.debug("sendVerificationEmail: {}", verificationEmail);

        Map<String, Object> data = new HashMap<>();
        data.put("receiverEmail", verificationEmail.getReceiverEmail());
        data.put("verificationCode", verificationEmail.getVerificationCode());
        data.put("verificationLink", activateBaseUrl + verificationEmail.getVerificationCode());

        String html = emailBuilder.buildHtml(VERIFICATION_EMAIL, data);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddress(verificationEmail.getReceiverEmail())
                .subject("Email verification")
                .body(html)
                .html(true)
                .build();
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }

    @Async
    @Override
    public void sendResetPasswordEmail(ResetPasswordEmail resetPasswordEmail) {
        log.debug("sendResetPasswordEmail: {}", resetPasswordEmail);

        Map<String, Object> data = new HashMap<>();
        data.put("receiverEmail", resetPasswordEmail.getReceiverEmail());
        data.put("resetKey", resetPasswordEmail.getUser().getResetKey());
        data.put("resetLink", resetPasswordFinishBaseUrl + resetPasswordEmail.getUser().getResetKey());

        String html = emailBuilder.buildHtml(RESET_PASSWORD_EMAIL, data);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddress(resetPasswordEmail.getReceiverEmail())
                .subject("Reset password")
                .body(html)
                .html(true)
                .build();
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }

    @Async
    @Override
    public void sendPasswordChangedEmail(PasswordChangedEmail passwordChangedEmail) {
        log.debug("sendPasswordChangedEmail: {}", passwordChangedEmail);
        String html = emailBuilder.buildHtml(PASSWORD_CHANGED_EMAIL, passwordChangedEmail);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddress(passwordChangedEmail.getReceiverEmail())
                .subject("Password changed email")
                .body(html)
                .html(true)
                .build();
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }

    @Async
    @Override
    public void sendUserCreatedEmail(User user, String password) {
        log.debug("userCreatedEmail.");
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("password", password);
        String html = emailBuilder.buildHtml(USER_CREATED_EMAIL, map);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddress(user.getEmail())
                .subject("Account created")
                .body(html)
                .html(true)
                .build();
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }

    @Override
    public void sendUserRemovedEmail(String userEmail) {
        log.debug("Sending UserRemovedEmail...");
        Map<String, Object> map = new HashMap<>();
        map.put("email", userEmail);
        String html = emailBuilder.buildHtml(USER_DELETED_EMAIL, map);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddress(userEmail)
                .subject("Account deleted")
                .body(html)
                .html(true)
                .build();
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }

    @Override
    public void sendCodeReaderNotification(Object data, File file, List<String> receivers) {
        log.debug("sendCodeReaderNotification.");
        String html = emailBuilder.buildHtml(CODEREADR_NOTIFICATION_EMAIL, data);
        log.trace(html);
        Email email = Email.builder()
                .senderAddress(emailSender)
                .receiverAddresses(receivers)
                .subject("Codereadr Notification Email")
                .body(html)
                .attachments(Collections.singletonList(file))
                .html(true)
                .build();
        EmailResult result = emailSenderService.send(email);
        log.info("Email sent successfully. {}", result);
    }

    @EventListener
    public void handleUserCreatedEvent(OnUserCreatedEvent e) {
        sendUserCreatedEmail(e.getUser(), e.getPassword());
    }

    @EventListener
    public void handleUserRegisteredEvent(OnUserRegisteredEvent e) {
        User user = e.getUser();
        if (!user.isActivated()) {
            sendVerificationEmail(VerificationEmail.builder()
                    .verificationCode(user.getActivationKey())
                    .receiverEmail(user.getEmail())
                    .build());
        }
    }
}
