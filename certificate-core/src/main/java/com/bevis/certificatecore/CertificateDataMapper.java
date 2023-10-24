package com.bevis.certificatecore;

import com.bevis.bevisassetpush.mapper.FileUploadResponseMapper;
import com.bevis.filecore.domain.File;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.master.domain.Master;
import com.bevis.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Deprecated
@Component
@RequiredArgsConstructor
public class CertificateDataMapper {

    private final FileUploadResponseMapper fileUploadResponseMapper;
    private final UserService userService;

    public Map<String, String> constructCertificateData(Master master, AssetDTO asset, File file) {
        Map<String, String> certificateData = new HashMap<>(fileUploadResponseMapper.mapAsset(master, asset, file));
        userService.findByAssetId(
                master.getOwnerAssetId()).ifPresent(user -> certificateData.put("userEmail", user.getEmail())
        );
        return certificateData;
    }

}
