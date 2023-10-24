package com.bevis.email;

import com.bevis.email.dto.Email;
import com.bevis.email.dto.EmailResult;

public interface EmailSenderService {
    EmailResult send(Email email);
}
