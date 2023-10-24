package com.bevis.assetimport.impl;

import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.AssetImportGatewayService;
import com.bevis.assetimport.BaseImportService;
import com.bevis.assetimport.codereadrservices.AssetImportNotificationService;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.assetimport.domain.enumeration.CodeReadrServiceType;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.repository.CodeReadrServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
class AssetImportGatewayServiceImpl implements AssetImportGatewayService {

    private final CodeReadrServiceRepository codeReadrServiceRepository;
    private final Map<CodeReadrServiceType, BaseImportService> codeReadrImportServices = new HashMap<>();
    private final AssetImportNotificationService assetImportNotificationService;

    @Autowired
    void initBaseImportServices(List<BaseImportService> services) {
        services.forEach(service -> codeReadrImportServices.put(service.getServiceKey(), service));
        log.debug("BaseImportServices setup");
    }

    @Override
    public AssetImport importAssets(AssetImportDTO assetImportDTO) throws AssetImportException {
        String serviceId = assetImportDTO.getServiceId();
        log.debug("Processing CodeReadr Service ID: {}", serviceId);
        return getCodeReadrImportServiceById(serviceId)
                .orElseThrow(() -> {
                    String error = "Service " + serviceId + " not registered.";
                    assetImportNotificationService.notifyImportFailed(assetImportDTO, error);
                    log.error(error);
                    return new AssetImportException(error);
                })
                .importAssets(assetImportDTO);
    }

    private Optional<BaseImportService> getCodeReadrImportServiceById(String serviceId) {
        return codeReadrServiceRepository.findByServiceId(serviceId)
                .map(CodeReadrService::getServiceKey)
                .map(codeReadrImportServices::get);
    }
}
