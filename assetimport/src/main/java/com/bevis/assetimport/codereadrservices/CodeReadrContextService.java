package com.bevis.assetimport.codereadrservices;

import com.bevis.assetimport.CodeReadrImportEventHandler;
import com.bevis.assetimport.mapper.CodeReadrQuestionsMapper;
import com.bevis.assetimport.mapper.DynamicAssetImportMapper;
import com.bevis.assetimport.repository.AssetImportRepository;
import com.bevis.asset.DynamicAssetService;
import com.bevis.master.MasterGeneratingService;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CodeReadrContextService {
    private final MasterService masterService;
    private final AssetImportRepository assetImportRepository;
    private final DynamicAssetService dynamicAssetService;
    private final DynamicAssetImportMapper dynamicAssetImportMapper;
    private final CodeReadrQuestionsMapper codeReadrQuestionsMapper;
    private final MasterGeneratingService masterGeneratingService;
    private final CodeReadrImportEventHandler codeReadrImportEventHandler;
    private final MasterPermissionCheckingService masterPermissionCheckingService;
    private final AssetImportNotificationService assetImportNotificationService;
}
