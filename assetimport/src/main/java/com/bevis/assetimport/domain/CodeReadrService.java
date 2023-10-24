package com.bevis.assetimport.domain;

import com.bevis.assetimport.domain.enumeration.CodeReadrServiceType;
import lombok.Data;

import javax.persistence.*;

@Table(name = "codereadr_service")
@Data
@Entity
public class CodeReadrService {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_key")
    @Enumerated(EnumType.STRING)
    private CodeReadrServiceType serviceKey;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "service_description")
    private String serviceDescription;

    @Column(name = "assets_table")
    private String assetsTable;

    @Column(name = "reimport_allowed")
    private Boolean reimportAllowed;

    @Column(name = "is_notify")
    private boolean isNotify;

    @Column(name = "parent_asset_key")
    private String parentAssetKey;
}
