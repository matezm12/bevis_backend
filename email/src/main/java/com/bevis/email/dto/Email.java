package com.bevis.email.dto;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
@Builder
public class Email {

    private String subject;

    private String body;

    private boolean html;

    private String senderAddress;

    private String receiverAddress;

    private List<String> receiverAddresses;

    private List<File> attachments;
}
