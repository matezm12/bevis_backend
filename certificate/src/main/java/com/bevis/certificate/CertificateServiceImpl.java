package com.bevis.certificate;

import com.bevis.certbuilder.AssetCertificateBuilder;
import com.bevis.certbuilder.dto.AssetCertificateContext;
import com.bevis.certificate.dto.CertAssetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.OutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
class CertificateServiceImpl implements CertificateService {

    private final AssetCertDataMappingService assetCertDataMappingService;
    private final AssetCertificateBuilder assetCertificateBuilder;
    private final CertificateDataLoadingService certificateDataLoadingService;

    @Override
    public void constructCertificateForAsset(String assetId, OutputStream outputStream) throws Exception {
        CertAssetDTO certAssetDTO = certificateDataLoadingService.loadCertAssetData(assetId);
        AssetCertificateContext assetCertificateContext = assetCertDataMappingService.loadAssetCertificateContext(certAssetDTO);
        assetCertificateBuilder.constructCertificate(assetCertificateContext, outputStream);
    }

}
