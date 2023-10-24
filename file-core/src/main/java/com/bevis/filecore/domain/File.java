package com.bevis.filecore.domain;

import com.bevis.filecore.domain.enumeration.FileState;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Data
@Entity(name = "files")
@EqualsAndHashCode(of = {"id"})
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "ipfs")
    private String ipfs;

    @Column(name = "blockchain")
    private String blockchain;

    @Column(name = "trans_id")
    private String transactionId;

    @Column(name = "block")
    private Long block;

    @Column(name = "sha256_hash")
    private String sha256Hash;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "encrypted")
    private Boolean encrypted;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private FileState state = FileState.UNCONFIRMED;

    @Column(name = "error")
    private String error;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "owner_asset_id")
    private String ownerAssetId;

    @Type(type = "json")
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    @Column(name = "credits_payment_id")
    private Long creditsPaymentId;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdDate = Instant.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant lastModifiedDate = Instant.now();
}
