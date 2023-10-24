package com.bevis.assettype.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Map;

@Table(name = "asset_types")
@Data
@Entity
public class AssetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`key`")
    private String key;

    private boolean deleted;

    @Column(name = "is_product", nullable = false)
    private Boolean isProduct = false;

    @Column(name = "is_csc", nullable = false)
    private Boolean isCsc = false;

    /**
     * This is the instance AssetType used for Products AssetTypes (isProduct=true)
     * When user create assets based on this product AssetType,
     * those assets is assigned with instanceAssetType
     */
    @OneToOne
    @JoinColumn(name = "instance_type_id")
    private AssetType instanceAssetType;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "fields_schema")
    private Map<String, Object> fieldsSchema;

}
