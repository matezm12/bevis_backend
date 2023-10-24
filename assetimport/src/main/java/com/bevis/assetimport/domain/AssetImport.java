package com.bevis.assetimport.domain;

import com.bevis.master.domain.Master;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Map;

@Data
@Entity
@Table(name = "assets_import")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class AssetImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scan_id")
    private String scanId;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "upc")
    private String upc;

    @Column(name = "username")
    private String username;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "scan_date")
    private Instant scanDate;

    @Column(name = "upload_date")
    private Instant uploadDate;

    @Column(name = "vendor_asset_id")
    private String vendorAssetId;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "assets_count")
    private Long assetsCount;

    @Column(name = "codereadr_body")
    private String codereadrBody;

    @Column(name = "error")
    private String error;

    @Column(name = "matched")
    private Boolean matched;

    @ManyToOne
    @JoinColumn(name = "sku")
    private Master sku;

    @Column(name = "created_at")
    @CreatedDate
    private Instant createdDate = Instant.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant lastModifiedDate = Instant.now();

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "attributes")
    private Map<String, Object> attributes;
}
