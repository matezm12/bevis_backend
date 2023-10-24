package com.bevis.master;

import com.bevis.master.domain.Master;

import java.util.List;

public interface MasterPermissionCheckingService {

    void validateAssetImportPermission(List<Master> master) throws PermissionDeniedException;
    void validateAssetImportPermission(Master master) throws PermissionDeniedException;
    void validateFilePublishPermission(Master master) throws PermissionDeniedException;
    void validateDynamicAssetEditPermission(String assetId) throws PermissionDeniedException;
    void validateDynamicAssetEditPermission(Master master) throws PermissionDeniedException;
    void validateCertificatePublishingPermission(Master master) throws PermissionDeniedException;
    void validateMasterLock(Master master) throws PermissionDeniedException;
}
