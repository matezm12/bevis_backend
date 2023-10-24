package com.bevis.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "default_currency")
    private String defaultCurrency;

    @JsonIgnore
    @Column(name = "password_digest")
    private String passwordDigest;

    @NotNull
    @Column(name = "activated", nullable = false)
    private boolean activated = false;

    @Size(max = 20)
    @JsonIgnore
    @Column(name = "activation_key")
    private String activationKey;

    @Size(max = 20)
    @JsonIgnore
    @Column(name = "reset_key")
    private String resetKey;

    @JsonIgnore
    @Column(name = "reset_date")
    private Instant resetDate = null;

    @Column(name = "authority")
    private String authority;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdDate = Instant.now();

    @Column(name = "vendor_asset_id")
    @CreatedDate
    private Instant vendorAssetId;

    @Column(name = "user_asset_id")
    private String userAssetId;

    @Column(name = "group_asset_id")
    private String groupAssetId;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant lastModifiedDate = Instant.now();

    @Column(name = "codereadr_device_id")
    private Long codereadrDeviceId;

    @Transient
    public List<String> getAuthorities() {
        return Collections.singletonList(authority);
    }
}
