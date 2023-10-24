package com.bevis.master.impl;

import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class MasterPermissionCheckingServiceImpl implements MasterPermissionCheckingService {

    private final MasterService masterService;

    @Override
    public void validateAssetImportPermission(List<Master> masters) throws PermissionDeniedException {
        for (Master master : masters) {
            validateAssetImportPermission(master);
        }
    }

    @Override
    public void validateAssetImportPermission(Master master) throws PermissionDeniedException {
        validateMasterLock(master);
    }

    @Override
    public void validateFilePublishPermission(Master master) throws PermissionDeniedException {
        validateMasterLock(master);
    }

    @Override
    public void validateDynamicAssetEditPermission(String assetId) throws PermissionDeniedException {
        Optional<Master> masterOpt = masterService.findById(assetId);
        if (masterOpt.isPresent()) {
            validateDynamicAssetEditPermission(masterOpt.get());
        }
    }

    @Override
    public void validateDynamicAssetEditPermission(Master master) throws PermissionDeniedException {
        validateMasterLock(master);
    }

    @Override
    public void validateCertificatePublishingPermission(Master master) throws PermissionDeniedException {
        validateMasterLock(master);
    }

    @Override
    public void validateMasterLock(Master master) throws PermissionDeniedException {
        if (Objects.nonNull(master.getIsLocked()) && master.getIsLocked()) {
            throw new PermissionDeniedException("Master " + master.getId() + " is write-protected!");
        }
    }
}
