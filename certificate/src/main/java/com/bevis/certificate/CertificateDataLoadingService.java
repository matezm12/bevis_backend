package com.bevis.certificate;

import com.bevis.assetinfo.AdminAssetGroupsInfoService;
import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.certificate.dto.CertAssetDTO;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.master.domain.Master;
import com.bevis.asset.AssetsRelationService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
class CertificateDataLoadingService {

    private final MasterService masterService;
    private final AdminAssetGroupsInfoService adminAssetGroupsInfoService;
    private final AssetsRelationService assetsRelationService;

    @Transactional(readOnly = true)
    public CertAssetDTO loadCertAssetData(String assetId) {
        Master master = masterService.findByIdOrPublicKey(assetId)
                .orElseThrow(ObjectNotFoundException::new);
        if (Objects.nonNull(master.getCertificate())) {
            throw new RuntimeException("Certificate already generated");
        }
        AssetGroupsInfoWrapper assetInfo = adminAssetGroupsInfoService.getAssetInfo(master);
        List<Master> childrenAssets = Collections.emptyList();//assetsRelationService.findChildrenOfAsset(master);
        List<Master> parentsAssets = assetsRelationService.findParentsOfAsset(master);
        return CertAssetDTO.builder()
                .master(master)
                .assetInfo(assetInfo)
                .childrenAssets(childrenAssets)
                .parentsAssets(parentsAssets)
                .build();
    }
}
