package com.bevis.certbuilder;

import com.bevis.certbuilder.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CertbuilderApplicationTests {

    @Autowired
    private AssetCertificateBuilder assetCertificateBuilder;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCertGen() throws Exception {
        AssetCertificateContext context = prepareCertificateContext();
        File file = new File("cert.html");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            assetCertificateBuilder.constructCertificate(context, fileOutputStream);
        }

        log.info("Finish");
    }

    private AssetCertificateContext prepareCertificateContext() throws Exception {
        AssetCertificateContext context = new AssetCertificateContext();
        context.setTitle("WSB Silver Antique");
        context.setSubtitle("WallStBets");
        context.setSubTitleLabel("Blockchain Mint");
        context.setLogo("https://ipfs.bevis.sg/ipfs/QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu");
        String publicKey = "qpzm39u2x5yfa9dsr6kgxew9hgt95y33psdtnm0s3c";
        context.setAssetId("pzm39u");
        context.setAssetUrl("https://www.bevis.sg/?asset-id=pzm39u");
        context.setPublicKey(publicKey);
        context.setQrcode(QrCodeUtil.generateBase64QrCodeFromText(publicKey));

        List<PropertyItem> masterGroup = new ArrayList<>();
        masterGroup.add(PropertyItem.of("Public Key", publicKey, "https://explore.bevis.sg/block-explorer?blockchain=BCH&address=qpzm39u2x5yfa9dsr6kgxew9hgt95y33psdtnm0s3c"));
        masterGroup.add(PropertyItem.of("Blockchain", "BCH", null));
        masterGroup.add(PropertyItem.of("Gen Date", "2020-05-04", null));
        masterGroup.add(PropertyItem.of("Asset Type", "Coin", null));
        masterGroup.add(PropertyItem.of("CodeReadr ScanId", "164613294", null));
        masterGroup.add(PropertyItem.of("Master Import", "Packtica Tube Stickers", null));
        masterGroup.add(PropertyItem.of("Vendor", "Rearden Metals Pte Ltd", null));
        context.setMasterGroup(masterGroup);

        List<GroupItem> assetGroups = new ArrayList<>();

        assetGroups.add(GroupItem.of("Asset", "Asset", "asset-section", getAssetProps(), getFiles()));

        assetGroups.add(GroupItem.of("Carton", "Carton", "carton-section", getAssetProps(), getFiles()));
        assetGroups.add(GroupItem.of("SKU", "SKU", "sku-section", getSkuAssetProps(), getFiles()));
        assetGroups.add(GroupItem.of("QC", "QC", "qc-section", getAssetProps(), getFiles()));
        assetGroups.add(GroupItem.of("VENDOR", "VENDOR", "vendor-section", getAssetProps(), getFiles()));
        assetGroups.add(GroupItem.of("LOCATION", "LOCATION", "location-section", getAssetProps(), getFiles()));
        context.setAssetGroups(assetGroups);

        List<RelatedAssetItem> childrenAssets = new ArrayList<>();

        childrenAssets.add(RelatedAssetItem.builder()
                .assetId("pf556z")
                .assetUrl("https://www.bevis.sg/?asset-id=pf556z")
                .publicKey("qpf556zxsfcfw9x8trtce5h0449wm98nhyh437g3xt")
                .publicKeyUrl("https://explore.bevis.sg/block-explorer?blockchain=BCH&address=qpf556zxsfcfw9x8trtce5h0449wm98nhyh437g3xt")
                .genDate("2020-04-30")
                .vendor("Rearden Metals Pte Ltd")
                .assetType("Carton")
                .build());

        context.setChildrenAssets(childrenAssets);
        context.setChildrenAssetsCount(childrenAssets.size());

        List<RelatedAssetItem> parentAssets = new ArrayList<>();

        parentAssets.add(RelatedAssetItem.builder()
                .assetId("pf556z")
                .assetUrl("https://www.bevis.sg/?asset-id=pf556z")
                .publicKey("qpf556zxsfcfw9x8trtce5h0449wm98nhyh437g3xt")
                .publicKeyUrl("https://explore.bevis.sg/block-explorer?blockchain=BCH&address=qpf556zxsfcfw9x8trtce5h0449wm98nhyh437g3xt")
                .genDate("2020-04-30")
                .vendor("Rearden Metals Pte Ltd")
                .assetType("Carton")
                .build());

        parentAssets.add(RelatedAssetItem.builder()
                .assetId("p2gu3p")
                .assetUrl("https://www.bevis.sg/?asset-id=p2gu3p")
                .publicKey("qp2gu3pegkr3rc275g6mlccvtn5vk20gusgvcms7sx")
                .publicKeyUrl("https://explore.bevis.sg/block-explorer?blockchain=BCH&address=qp2gu3pegkr3rc275g6mlccvtn5vk20gusgvcms7sx")
                .genDate("2020-04-30")
                .vendor("Rearden Metals Pte Ltd")
                .assetType("Operator")
                .build());

        parentAssets.add(RelatedAssetItem.builder()
                .assetId("p52e9d")
                .assetUrl("https://www.bevis.sg/?asset-id=p52e9d")
                .publicKey("qp52e9dutckvk47mqyzr07yldq2qzldd9s8jrw5846")
                .publicKeyUrl("https://explore.bevis.sg/block-explorer?blockchain=BCH&address=qp52e9dutckvk47mqyzr07yldq2qzldd9s8jrw5846")
                .genDate("2020-04-30")
                .vendor("Rearden Metals Pte Ltd")
                .assetType("Vendor")
                .build());

        context.setParentAssets(parentAssets);
        context.setParentAssetsCount(parentAssets.size());

        String description = "";
        context.setDescription(description);

        List<SectionItem> sections = new ArrayList<>();
        sections.add(SectionItem.of("master-section", "MASTER"));
        sections.addAll(assetGroups.stream()
                .map(x -> SectionItem.of(x.getSection(), x.getName()))
                .collect(Collectors.toList()));
        sections.add(SectionItem.of("related-section", "Related Assets"));
        context.setSections(sections);
        return context;
    }

    private List<FileItem> getFiles() {
        List<FileItem> fileItems = new ArrayList<>();
        fileItems.add(FileItem.of("QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu", "https://ipfs.bevis.sg/ipfs/QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu", "d022c92c8a2e3c51010f53830eb8de41cb8dab2c57766e374b6f32398c70b7cc", "https://explore.bevis.sg/block-explorer?blockchain=BCH&tx=d022c92c8a2e3c51010f53830eb8de41cb8dab2c57766e374b6f32398c70b7cc", "https://ipfs.bevis.sg/ipfs/QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu", PreviewMode.IMG));
        fileItems.add(FileItem.of("QmVjmqzfQR7aLPBD6nhWSkNn1gjW4zK29igEqkUH5Y3vdo", "https://ipfs.bevis.sg/ipfs/QmVjmqzfQR7aLPBD6nhWSkNn1gjW4zK29igEqkUH5Y3vdo", "22863c68254b970f276760a4dc9098dd60ceb658a68dd0fe46957c8948ed1242", "https://explore.bevis.sg/block-explorer?blockchain=BCH&tx=22863c68254b970f276760a4dc9098dd60ceb658a68dd0fe46957c8948ed1242", "https://ipfs.bevis.sg/ipfs/QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu", PreviewMode.IMG));
        fileItems.add(FileItem.of("QmbKuDrBpLFseYobYv71nCJGseDWLHqTx5ParMgaYCkhqb", "https://ipfs.bevis.sg/ipfs/QmbKuDrBpLFseYobYv71nCJGseDWLHqTx5ParMgaYCkhqb", "4fa8ef77d8c8ab0ef3e5a059c65ee0c7b22e2058d47616b6e8d0a4b5691c42fd", "https://explore.bevis.sg/block-explorer?blockchain=BCH&tx=4fa8ef77d8c8ab0ef3e5a059c65ee0c7b22e2058d47616b6e8d0a4b5691c42fd", "https://ipfs.bevis.sg/ipfs/QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu", PreviewMode.IMG));
        fileItems.add(FileItem.of("QmbNJXaX46XRpiaAtAjK3KeToAJb8BbD5gAQU3ts9cdVJJ", "https://ipfs.bevis.sg/ipfs/QmbNJXaX46XRpiaAtAjK3KeToAJb8BbD5gAQU3ts9cdVJJ", "5921c37ba6a73de0b571c22a10ee2df0c415c0ed468f3f37c42f1388d8038760", "https://explore.bevis.sg/block-explorer?blockchain=BCH&tx=5921c37ba6a73de0b571c22a10ee2df0c415c0ed468f3f37c42f1388d8038760", "https://ipfs.bevis.sg/ipfs/QmePhCYDxSQsGeVCr72Thwt1gqxPqTpDVbWsmiiyBikuLu", PreviewMode.IMG));
        return fileItems;
    }

    private List<PropertyItem> getAssetProps() {
        List<PropertyItem> list = new ArrayList<>();
        list.add(PropertyItem.of("Asset ID", "pzm39u", "https://www.bevis.sg/?asset-id=pzm39u"));
        list.add(PropertyItem.of("Carton asset ID", "pf556z", "https://www.bevis.sg/?asset-id=pf556z"));
        list.add(PropertyItem.of("Date pack", "2021-03-18", null));
        list.add(PropertyItem.of("Import id", "160541593", null));
        list.add(PropertyItem.of("Product type asset id", "p2gteq", "https://www.bevis.sg/?asset-id=p2gteq"));
        return list;
    }

    private List<PropertyItem> getSkuAssetProps() {
        List<PropertyItem> list = new ArrayList<>();
        list.add(PropertyItem.of("Asset ID", "qax2z3", "https://www.bevis.sg/?asset-id=qax2z3"));
        list.add(PropertyItem.of("Artist", "Ashton Gray", null));
        list.add(PropertyItem.of("Authorized", "1000", null));
        list.add(PropertyItem.of("Collection", "WallStBets", null));
        list.add(PropertyItem.of("Edging", "laser-etched", null));
        list.add(PropertyItem.of("Finish", "antique", null));
        list.add(PropertyItem.of("Material", "999+ Silver", null));
        list.add(PropertyItem.of("Name", "WSB Silver Antique", null));
        list.add(PropertyItem.of("Obverse", "WallStBets", null));
        list.add(PropertyItem.of("Packaging", "Blockchain Mint", null));
        list.add(PropertyItem.of("Reverse", "Rocket Ship", null));
        list.add(PropertyItem.of("Sculptor", "Ghim Ong", null));
        list.add(PropertyItem.of("Sku", "wsb-ant", null));
        list.add(PropertyItem.of("Toz", "1.00", null));
        list.add(PropertyItem.of("Upc", "764283495235", null));
        list.add(PropertyItem.of("Weight", "31.1", null));
        return list;
    }

}
