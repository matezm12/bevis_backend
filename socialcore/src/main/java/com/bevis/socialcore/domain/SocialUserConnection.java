package com.bevis.socialcore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "social_user_connection")
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String providerId;

    private String providerUserId;

    @Column(name = "`rank`")
    private Long rank;

    private String displayName;

    private String imageUrl;


}
