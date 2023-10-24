package com.bevis.assetimport.impl;

import com.bevis.assetimport.CodeReaderNotificationSender;
import com.bevis.assetimport.CodeReadrImportEventHandler;
import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.config.ConfigKey;
import com.bevis.config.ConfigService;
import com.bevis.master.domain.Master;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bevis.common.util.DateUtil.convertInstantToDateString;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeReadrImportEventHandlerImpl implements CodeReadrImportEventHandler {

    private final ConfigService configService;
    private final CodeReaderNotificationSender codeReaderNotificationSender;

    @Async
    @Override
    public void onAssetImportPrepared(ImportWrappingDTO importWrappingDTO) {
        log.debug("sending AssetImport notification...");
        try {
            List<Master> masters = importWrappingDTO.getMasters();
            String[] receivers = configService.getValue(ConfigKey.CODEREADR_NOTIFICATION_RECEIVERS).split("\\|");
            String assetIdsData = masters.stream().map(Master::getId)
                    .collect(Collectors.joining("\n")) + '\n';
            String date = convertInstantToDateString(importWrappingDTO.getDynamicAssetImport().getScanTime());
            File file = File.createTempFile("file_", "_" + date + ".txt");
            FileUtils.writeStringToFile(file, assetIdsData, Charset.defaultCharset());
            Map<String, String> data = prepareNotificationData(importWrappingDTO, receivers);
            codeReaderNotificationSender.sendCodeReaderNotification(data, file, Arrays.asList(receivers));
            boolean delete = file.delete();
            log.trace("Temp file deleted: {}", delete);
            log.debug("AssetImport Notification sent successfully!");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> prepareNotificationData(ImportWrappingDTO importWrappingDTO, String[] receivers) {
        Map<String, String> data = new HashMap<>();
        data.put("emails", String.join(",", receivers));
        data.put("username", importWrappingDTO.getDynamicAssetImport().getUsername());
        data.put("scansCount", String.valueOf(importWrappingDTO.getMasters().size()));
        data.put("serviceId", importWrappingDTO.getDynamicAssetImport().getServiceId());
        data.put("deviceId", String.valueOf(importWrappingDTO.getDynamicAssetImport().getDeviceId()));
        data.put("scanId", importWrappingDTO.getDynamicAssetImport().getScanId());
        return data;
    }
}
