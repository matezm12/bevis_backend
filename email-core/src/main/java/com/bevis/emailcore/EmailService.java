package com.bevis.emailcore;

import com.bevis.user.domain.User;
import com.bevis.emailcore.dto.PasswordChangedEmail;
import com.bevis.emailcore.dto.ResetPasswordEmail;
import com.bevis.emailcore.dto.VerificationEmail;
import com.bevis.user.dto.OnUserCreatedEvent;
import com.bevis.user.dto.OnUserRegisteredEvent;
import org.springframework.context.event.EventListener;

public interface EmailService {
    void sendVerificationEmail(VerificationEmail verificationEmail);

    void sendResetPasswordEmail(ResetPasswordEmail resetPasswordEmail);

    void sendPasswordChangedEmail(PasswordChangedEmail passwordChangedEmail);

    void sendUserCreatedEmail(User user, String password);

    void sendUserRemovedEmail(String userEmail);

    @EventListener
    void handleUserCreatedEvent(OnUserCreatedEvent e);

    @EventListener
    void handleUserRegisteredEvent(OnUserRegisteredEvent e);
}
