package com.bevis.assettype;

import com.bevis.assettype.domain.AssetType;
import com.bevis.common.exception.PermissionDeniedException;
import com.bevis.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.bevis.assettype.AssetTypeSpecification.bySearchQuery;
import static com.bevis.security.AuthoritiesConstants.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
class AssetTypesMgmtServiceImpl implements AssetTypesMgmtService {
    private final AssetTypesRepository assetTypeRepository;

    @Secured({ADMIN, VENDOR, OPERATOR})
    @Transactional(readOnly = true)
    @Override
    public Optional<AssetType> findById(Long id) {
        return assetTypeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AssetType> findAll() {
        return assetTypeRepository.findAllByDeleted(false);
    }

    @Secured({ADMIN, VENDOR, OPERATOR})
    @Transactional(readOnly = true)
    @Override
    public Page<AssetType> findAll(Pageable pageable) {
        if (SecurityUtils.isCurrentUserAdmin()) {
            return assetTypeRepository.findAllByDeleted(false, pageable);
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public Page<AssetType> searchAll(String search, Pageable pageable) {
        if (SecurityUtils.isCurrentUserAdmin()) {
            return assetTypeRepository.findAll(bySearchQuery(search), pageable);
        } else {
            return new PageImpl<>(Collections.emptyList());
        }
    }

    @Override
    public AssetType save(AssetType assetType) {
        return assetTypeRepository.saveAndFlush(assetType);
    }

    @Override
    public void deleteById(Long id) {
        assetTypeRepository.deleteById(id);
    }
}
