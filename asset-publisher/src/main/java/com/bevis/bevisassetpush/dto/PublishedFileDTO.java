package com.bevis.bevisassetpush.dto;

import com.bevis.credits.domain.CreditsPayment;
import com.bevis.user.domain.User;
import com.bevis.files.dto.File;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PublishedFileDTO {
    private File file;
    private User user;
    private Boolean encrypted;
    private FileParametersDTO params;
    private Map<String, Object> metadata;
    private CreditsPayment creditsPayment;
}
