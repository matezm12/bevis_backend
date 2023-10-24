package com.bevis.assetimport.codereadrservices;

import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.events.EventPublishingService;
import com.bevis.events.dto.UserActionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetImportNotificationServiceImpl implements AssetImportNotificationService {

    private final EventPublishingService eventPublishingService;

    @Async
    @Override
    public void notifyImportCreated(ImportWrappingDTO importWrappingDTO) {
        log.debug("notifyImportCreated");
        AssetImportDTO dynamicAssetImport = importWrappingDTO.getDynamicAssetImport();
        eventPublishingService.publishEventAsync(UserActionEvent.builder()
                .action("CodeREADR Import")
                .actionStatus(UserActionEvent.ActionStatus.SUCCESS)
                .user("Vendor: " + dynamicAssetImport.getUsername())
                .details(buildImportCreatedDetails(importWrappingDTO))
                .link("http://localhost:9000/entity/asset-import?search=" + dynamicAssetImport.getScanId())
                .build()
        );
    }

    @Override
    public void notifyImportFailed(AssetImportDTO dynamicAssetImport , String error) {
        log.debug("notifyImportFailed");
        eventPublishingService.publishEventAsync(UserActionEvent.builder()
                .action("CodeREADR Import")
                .actionStatus(UserActionEvent.ActionStatus.FAILURE)
                .user("Vendor: " + dynamicAssetImport.getUsername())
                .details(buildImportFailedDetails(dynamicAssetImport, error))
                .link("http://localhost:9000/entity/asset-import?search=" + dynamicAssetImport.getScanId())
                .build()
        );
    }

    private String buildImportCreatedDetails(ImportWrappingDTO importWrappingDTO) {
        AssetImportDTO dynamicAssetImport = importWrappingDTO.getDynamicAssetImport();
        return "\nImport Success \n\n" +
                "Scan ID: " + dynamicAssetImport.getScanId() + "\n" +
                "Service ID: " + dynamicAssetImport.getServiceId() + "\n" +
                "Device ID: " + dynamicAssetImport.getDeviceId() + "\n" +
                "ScanTime: " + dynamicAssetImport.getScanTime() + "\n\n" +
                "BarcodeItems:\n" + dynamicAssetImport.getBarcodeItems() + "\n\n" +
                "Arguments:\n" + dynamicAssetImport.getArguments();
    }

    private String buildImportFailedDetails(AssetImportDTO dynamicAssetImport, String error) {
        return "\nImport FAILURE \n\n" +
                "Error: " + error + "\n\n" +
                "Scan ID: " + dynamicAssetImport.getScanId() + "\n" +
                "Service ID: " + dynamicAssetImport.getServiceId() + "\n" +
                "Device ID: " + dynamicAssetImport.getDeviceId() + "\n" +
                "ScanTime: " + dynamicAssetImport.getScanTime() + "\n\n" +
                "BarcodeItems:\n" + dynamicAssetImport.getBarcodeItems() + "\n\n" +
                "Arguments:\n" + dynamicAssetImport.getArguments();
    }
}
