package com.bevis.certificate;

import com.bevis.assetinfo.dto.AssetGroupsInfoWrapper;
import com.bevis.assetinfo.dto.AssetValue;
import com.bevis.assetinfo.dto.FileAssetGroup;
import com.bevis.assettype.domain.AssetType;
import com.bevis.blockchaincore.BlockchainUrlGateway;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.blockchainfile.dto.FileDTO;
import com.bevis.certbuilder.QrCodeUtil;
import com.bevis.certbuilder.dto.*;
import com.bevis.certificate.dto.CertAssetDTO;
import com.bevis.common.util.DateUtil;
import com.bevis.filecode.domain.enumeration.FileCodeGroup;
import com.bevis.gateway.UrlGatewayService;
import com.bevis.master.domain.Master;
import com.bevis.master.domain.MasterImport;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AssetCertDataMappingService {

    private static final Set<String> EXCLUDED_GROUP_KEYS = Set.of("productTypeAssetId");

    private final UrlGatewayService urlGatewayService;
    private final BlockchainUrlGateway blockchainUrlGateway;

    public AssetCertificateContext loadAssetCertificateContext(CertAssetDTO certAssetDTO) throws Exception {
        AssetGroupsInfoWrapper assetInfo = certAssetDTO.getAssetInfo();

        FileAssetGroup skuGroup = findSkuGroup(assetInfo);

        AssetCertificateContext assetCertificateContext = new AssetCertificateContext();
        assetCertificateContext.setTitle(findPropertyInGroup(skuGroup, "obverse"));
        assetCertificateContext.setSubtitle(findPropertyInGroupOpt(skuGroup, "collection")
            .orElse(findPropertyInGroup(skuGroup, "description")));
        assetCertificateContext.setSubTitleLabel(findPropertyInGroupOpt(skuGroup, "name")
                .orElse(findPropertyInGroup(skuGroup, "metal")));

        assetCertificateContext.setLogo(assetInfo.getLogo());
        assetCertificateContext.setAssetId(assetInfo.getAssetId());
        assetCertificateContext.setAssetUrl(urlGatewayService.getAssetViewerLink(assetInfo.getAssetId()));
        assetCertificateContext.setPublicKey(assetInfo.getPublicKey());
        assetCertificateContext.setPublicKeyUrl(assetInfo.getPublicKeyUrl());
        assetCertificateContext.setQrcode(QrCodeUtil.generateBase64QrCodeFromText(assetInfo.getPublicKey()));

        assetCertificateContext.setMasterGroup(getMasterGroup(certAssetDTO.getMaster()));

        List<GroupItem> assetGroups = getAssetGroups(assetInfo.getAssetGroups());
        assetCertificateContext.setAssetGroups(assetGroups);

        List<RelatedAssetItem> childrenAssets = getRelatedAssets(certAssetDTO.getChildrenAssets());
        assetCertificateContext.setChildrenAssets(childrenAssets);
        assetCertificateContext.setChildrenAssetsCount(childrenAssets.size());

        List<RelatedAssetItem> parentAssets = getRelatedAssets(certAssetDTO.getParentsAssets());
        assetCertificateContext.setParentAssets(parentAssets);
        assetCertificateContext.setParentAssetsCount(parentAssets.size());

        assetCertificateContext.setSections(
                assetGroups.stream().map(x -> SectionItem.of(x.getSection(), x.getName())).collect(Collectors.toList())
        );

        return assetCertificateContext;
    }

    private FileAssetGroup findSkuGroup(AssetGroupsInfoWrapper assetInfo) {
        return findAssetGroupByName(assetInfo, "SKU")
                .orElseGet(() -> findAssetGroupByName(assetInfo, "ASSET").orElse(null));
    }

    private Optional<FileAssetGroup> findAssetGroupByName(AssetGroupsInfoWrapper assetInfo, String groupName) {
        return Optional.ofNullable(assetInfo)
                .map(AssetGroupsInfoWrapper::getAssetGroups)
                .orElse(Collections.emptyList())
                .stream()
                .filter(x -> groupName.equalsIgnoreCase(x.getGroupName()))
                .findFirst();
    }

    private Optional<String> findPropertyInGroupOpt(FileAssetGroup group, String propertyName) {
        return Optional.ofNullable(group)
                .map(FileAssetGroup::getAssetValues)
                .orElse(Collections.emptyList())
                .stream()
                .filter(x->propertyName.equalsIgnoreCase(x.getFieldName()))
                .findFirst()
                .map(AssetValue::getFieldValue);
    }

    private String findPropertyInGroup(FileAssetGroup group, String propertyName) {
        return findPropertyInGroupOpt(group, propertyName).orElse(Strings.EMPTY);
    }

    private List<RelatedAssetItem> getRelatedAssets(List<Master> assets) {
        return assets.stream()
                .map(x -> RelatedAssetItem.builder()
                        .assetId(x.getId())
                        .assetUrl(urlGatewayService.getAssetViewerLink(x.getId()))
                        .publicKey(x.getPublicKey())
                        .publicKeyUrl(getPublicKeyUrl(x))
                        .assetType(Optional.of(x)
                                .map(Master::getAssetType)
                                .map(AssetType::getName)
                                .orElse(Strings.EMPTY))
                        .genDate(formatInstantToDate(x.getGenDate()))
                        .build())
                .collect(Collectors.toList());
    }

    private String getPublicKeyUrl(Master master) {
        Blockchain blockchain = Optional.of(master)
                .map(Master::getBlockchain)
                .orElse(null);
        return blockchainUrlGateway.getBlockchainAddressLink(master.getPublicKey(), blockchain);
    }

    private List<GroupItem> getAssetGroups(List<FileAssetGroup> assetGroups) {
        return assetGroups.stream()
                .map(x -> GroupItem.of(
                        x.getGroupKey(),
                        x.getGroupName(),
                        x.getGroupKey() + "-section",
                        mapProperties(x.getAssetValues()),
                        mapFiles(x.getFiles())
                ))
                .filter(x -> !EXCLUDED_GROUP_KEYS.contains(x.getKey()))
                .collect(Collectors.toList());
    }

    private List<PropertyItem> mapProperties(List<AssetValue> assetValues) {
        return assetValues.stream()
                .filter(x -> Objects.nonNull(x.getFieldValue()) && !isFieldIgnored(x.getFieldName()))
                .map(x -> PropertyItem.builder()
                        .title(x.getFieldTitle())
                        .value(x.getFieldValue())
                        .url(x.getLink())
                        .build())
                .collect(Collectors.toList());
    }

    private boolean isFieldIgnored(String fieldName) {
        String[] ignoredList = new String[]{"vendor_id", "asset_type_id", "asset_type_key", "id", "location_id"};
        Set<String> set = new HashSet<>(Arrays.asList(ignoredList));
        return set.contains(fieldName);
    }

    private List<FileItem> mapFiles(List<FileDTO> files) {
        return files.stream()
                .map(x -> FileItem.builder()
                        .ipfs(x.getIpfsHash())
                        .ipfsUrl(x.getUrl())
                        .previewMode(x.getFileGroup() == FileCodeGroup.DOCUMENT ? PreviewMode.IFRAME : PreviewMode.IMG)
                        .previewUrl(getPreviewUrl(x))
                        .tx(x.getTransactionId())
                        .txUrl(x.getTxUrl())
                        .build())
                .collect(Collectors.toList());
    }

    private String getPreviewUrl(FileDTO x) {
        switch (x.getFileGroup()) {
            case IMAGE:
                return x.getUrl();
            case DOCUMENT:
                return "https://docs.google.com/gview?embedded=true&url=" + x.getUrl();
            default:
                return x.getPlaceholderUrl();
        }
    }

    private List<PropertyItem> getMasterGroup(Master master) {
        List<PropertyItem> masterGroup = new ArrayList<>();

        String blockchainAddressLink = blockchainUrlGateway.getBlockchainAddressLink(master.getPublicKey(), master.getBlockchain());
        masterGroup.add(PropertyItem.of("Public Key", master.getPublicKey(), blockchainAddressLink));

        String blockchainName = Optional.of(master)
                .map(Master::getBlockchain)
                .map(Blockchain::getName)
                .orElse(Strings.EMPTY);
        masterGroup.add(PropertyItem.of("Blockchain", blockchainName, null));

        masterGroup.add(PropertyItem.of("Gen Date", formatInstantToDate(master.getGenDate()), null)); //fixme

        String assetTypeName = Optional.of(master)
                .map(Master::getAssetType)
                .map(AssetType::getName)
                .orElse(Strings.EMPTY);
        masterGroup.add(PropertyItem.of("Asset Type", assetTypeName, null));

        if (Objects.nonNull(master.getCodereadrScanId())) {
            masterGroup.add(PropertyItem.of("CodeReadr ScanId", master.getCodereadrScanId(), null));
        }

        if (Objects.nonNull(master.getMasterImport())) {
            String masterImportName = Optional.of(master)
                    .map(Master::getMasterImport)
                    .map(MasterImport::getName)
                    .orElse(Strings.EMPTY);
            masterGroup.add(PropertyItem.of("Master Import", masterImportName, null));
        }

        return masterGroup;
    }

    private String formatInstantToDate(Instant instant) {
        return DateUtil.convertInstantToDateString(instant);
    }

}
