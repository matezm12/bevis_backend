package com.bevis.master.domain;

import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.filecore.domain.File;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Master {

    @Id
    @Column(name = "asset_id")
    private String id;

    private String publicKey;

    private Instant genDate;

    @Column(name = "import_date")
    private Instant importDate;

    @JoinColumn(name = "asset_type")
    @ManyToOne
    private AssetType assetType;

    @JsonIgnore
    @JoinColumn(name = "import_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MasterImport masterImport;

    @Column(name = "codereadr_scan_id")
    private String codereadrScanId;

    @Column(name = "activation_status")
    private Boolean activationStatus;

    @Column(name = "is_csc")
    private Boolean isCsc = false;

    @ManyToOne
    private Blockchain blockchain;

    @ManyToOne
    private File file;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private File certificate;

    @Column(name = "creator_asset_id")
    @CreatedBy
    private String creatorAssetId;

    @Column(name="owner_asset_id")
    private String ownerAssetId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "asset_id", referencedColumnName = "asset_id")
    private Asset asset;

    @Column(name = "is_locked")
    private Boolean isLocked = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
