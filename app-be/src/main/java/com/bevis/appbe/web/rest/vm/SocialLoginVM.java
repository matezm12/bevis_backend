package com.bevis.appbe.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class SocialLoginVM {
    @NotNull
    private String accessToken;

    private String email;

    private Map<String, String> metadata;

}
