package com.bevis.certificate.dto;

import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.master.domain.Master;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CertAssetDTO {
    private Master master;
    private AssetGroupsInfoWrapper assetInfo;
    private List<Master> childrenAssets;
    private List<Master> parentsAssets;
}
