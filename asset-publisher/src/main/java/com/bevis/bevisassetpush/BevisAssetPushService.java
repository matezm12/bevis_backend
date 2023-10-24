package com.bevis.bevisassetpush;

import com.bevis.bevisassetpush.dto.AssetDocumentDTO;
import com.bevis.bevisassetpush.dto.BevisAssetDTO;
import com.bevis.bevisassetpush.dto.PublishedFileDTO;
import com.bevis.bevisassetpush.dto.PublishedIpfsDTO;
import com.bevis.asset.DynamicAssetException;
import com.bevis.filecore.domain.File;
import com.bevis.master.PermissionDeniedException;

import java.util.Map;


public interface BevisAssetPushService {

    @Deprecated
    BevisAssetDTO checkAssetByAssetId(String assetId);

    Map<String, String> uploadFileToNewAssetForUser(AssetDocumentDTO assetDocumentDTO) throws DynamicAssetException, PermissionDeniedException;

    Map<String, String> uploadFileToExistedAssetForUser(AssetDocumentDTO assetDocumentDTO) throws DynamicAssetException, PermissionDeniedException;

    File publishFileToAsset(String assetId, PublishedFileDTO publishedFileDTO) throws PermissionDeniedException;

    File publishIpfsToAsset(String assetId, PublishedIpfsDTO publishedIpfsDTO) throws PermissionDeniedException;

}
