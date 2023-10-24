package com.bevis.user.dto;

import lombok.Data;

@Data
public class UserFilter {
    private String search;
    private String authority;
    private Boolean activatedOnly;
}
