package com.bevis.assetimport.codereadrservices;

import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assettype.domain.AssetType;
import com.bevis.master.domain.Master;
import com.bevis.assetimport.domain.enumeration.CodeReadrServiceType;
import com.bevis.asset.dto.AssetRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class NotificationImportServiceImpl extends DynamicImportServiceImpl {

    public static final CodeReadrServiceType SERVICE_KEY = CodeReadrServiceType.NOTIFICATION_PREP;

    protected NotificationImportServiceImpl(CodeReadrContextService codeReadrContextService, AssetImportContextLoader assetImportContextLoader) {
        super(codeReadrContextService, assetImportContextLoader);
    }

    @Override
    public AssetType loadAssetType(AssetImport assetImport, ImportWrappingDTO assetImportDTO) {
        return null;
    }

    @Override
    public AssetRequest getProductRequestFromImport(String assetId, ImportWrappingDTO assetImportDTO) {
        return null;
    }

    @Override
    protected List<Master> importMasters(List<Master> masters, ImportWrappingDTO importWrappingDTO) {
        return Collections.emptyList(); //Masters readonly and cannot be imported
    }

    @Override
    public CodeReadrServiceType getServiceKey() {
        return SERVICE_KEY;
    }

}
