package com.bevis.certbuilder.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssetCertificateContext {
    private String title;
    private String subtitle;
    private String subTitleLabel;
    private String logo;
    private String assetId;
    private String assetUrl;
    private String publicKey;
    private String publicKeyUrl;
    private String qrcode;

    private List<PropertyItem> masterGroup;
    private List<GroupItem> assetGroups;

    private List<RelatedAssetItem> childrenAssets;
    private List<RelatedAssetItem> parentAssets;
    private Integer childrenAssetsCount;
    private Integer parentAssetsCount;

    private String description;

    private List<SectionItem> sections;
}
