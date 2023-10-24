package com.bevis.bevisassetpush;

import com.bevis.bevisassetpush.dto.FilePostDTO;
import com.bevis.bevisassetpush.dto.IpfsPostDTO;
import com.bevis.filecore.domain.File;
import com.bevis.master.PermissionDeniedException;

public interface AssetFilePublisherService {
    File publishFileToAsset(FilePostDTO filePostDTO) throws PermissionDeniedException;

    File publishIpfsToAsset(IpfsPostDTO ipfsPostDTO) throws PermissionDeniedException;

    File reTryTransactionManually(File fileDTO) throws PermissionDeniedException;
}
