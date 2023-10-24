package com.bevis.email;


import com.amazonaws.services.simpleemail.model.RawMessage;
import com.bevis.email.dto.Email;

interface RawMessageBuilder {
    RawMessage buildRawMessage(Email email);
}
