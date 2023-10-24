package com.bevis.social.core.dto;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserProfile implements Serializable {
    private final String id;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String username;

    public UserProfile(String id, String name, String firstName, String lastName, String email, String username) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
    }

}
