package com.bevis.asset.impl;

import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.asset.domain.AssetsRelation;
import com.bevis.master.domain.Master;
import com.bevis.asset.repository.AssetsRelationRepository;
import com.bevis.asset.AssetsRelationService;
import com.bevis.master.MasterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
class AssetsRelationServiceImpl implements AssetsRelationService {

    private final AssetsRelationRepository assetsRelationRepository;
    private final MasterService masterService;

    @Transactional(readOnly = true)
    @Override
    public boolean exists(String assetId, String parentAssetId) {
        return assetsRelationRepository.existsByAssetIdAndParentAssetId(assetId, parentAssetId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean exists(Master asset, Master parentAsset) {
        return assetsRelationRepository.existsByAssetAndParentAsset(asset, parentAsset);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findChildrenOfAsset(Master master) {
        return findChildrenOfAsset(master, false);
    }

    @Override
    public List<Master> findChildrenOfAsset(Master master, boolean recursively) {
        return findChildrenOfAssetId(master.getId(), recursively);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findChildrenOfAssetId(String assetId) {
        return findChildrenOfAssetId(assetId, false);
    }

    @Override
    public Page<Master> findChildrenOfAssetId(String assetId, Pageable pageable) {
        return assetsRelationRepository.findByParentAssetId(assetId, pageable)
                .map(AssetsRelation::getAsset);
    }

    @Override
    public List<Master> findChildrenOfAssetId(String assetId, boolean recursively) {
        List<Master> assetsFound = assetsRelationRepository.findByParentAssetId(assetId)
                .stream()
                .map(AssetsRelation::getAsset)
                .collect(Collectors.toList());
        List<Master> allAssets = new ArrayList<>(assetsFound);
        if (recursively && assetsFound.size() > 0) {
            for (Master asset: assetsFound) {
                allAssets.addAll(findChildrenOfAssetId(asset.getId(), recursively));
            }
        }
        return allAssets;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findParentsOfAsset(Master master) {
        return findParentsOfAsset(master, false);
    }

    @Override
    public List<Master> findParentsOfAsset(Master master, boolean recursively) {
        return findParentsOfAssetId(master.getId(), recursively);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Master> findParentsOfAssetId(String assetId) {
        return findParentsOfAssetId(assetId, false);
    }

    @Override
    public Page<Master> findParentsOfAssetId(String assetId, Pageable pageable) {
        return assetsRelationRepository.findByAssetId(assetId, pageable)
                .map(AssetsRelation::getParentAsset);
    }

    @Override
    public List<Master> findParentsOfAssetId(String assetId, boolean recursively) {
        List<Master> assetsFound = assetsRelationRepository.findByAssetId(assetId)
                .stream()
                .map(AssetsRelation::getParentAsset)
                .collect(Collectors.toList());
        List<Master> allAssets = new ArrayList<>(assetsFound);
        if (recursively && assetsFound.size() > 0) {
            for (Master asset: assetsFound) {
                allAssets.addAll(findParentsOfAssetId(asset.getId(), recursively));
            }
        }
        return allAssets;
    }

    @Override
    public void saveRelation(Master asset, Master parentAsset) {
        if (Objects.equals(asset.getId(), parentAsset.getId())) {
            log.warn("Can not save relation where assetId EQUAL TO parentAssetId");
            return;
        }
        AssetsRelation assetsRelation = new AssetsRelation();
        assetsRelation.setAsset(asset);
        assetsRelation.setParentAsset(parentAsset);
        assetsRelationRepository.save(assetsRelation);
    }

    @Override
    public void saveRelations(String assetId, Collection<String> parentAssetIds) {
        Master masterAsset = masterService.findById(assetId)
                .orElseThrow(() -> new ObjectNotFoundException("Asset ID:" + assetId + " not found"));
        Set<String> parentAssetsIdsSet = new HashSet<>(parentAssetIds);
        List<AssetsRelation> assetsRelations = parentAssetsIdsSet.stream()
                .filter(parentAssetId -> !Objects.equals(parentAssetId, assetId))
                .map(masterService::findById)
                .flatMap(Optional::stream)
                .map(parentAsset -> createAssetsRelation(masterAsset, parentAsset))
                .collect(Collectors.toList());
        if (!assetsRelations.isEmpty()) {
            assetsRelationRepository.saveAll(assetsRelations);
        }
    }

    @Override
    public void deleteRelationsByAssetId(String assetId, Collection<String> parentAssetIds) {
        assetsRelationRepository.deleteByAssetIdAndParentAssetIdIn(assetId, parentAssetIds);
    }

    @Override
    public void deleteByAssetId(String assetId) {
        assetsRelationRepository.deleteByAssetId(assetId);
    }

    @Override
    public Long getChildrenCount(String assetId) {
        return assetsRelationRepository.countByParentAssetId(assetId);
    }

    @NotNull
    private AssetsRelation createAssetsRelation(Master masterAsset, Master parentAsset) {
        AssetsRelation assetsRelation = new AssetsRelation();
        assetsRelation.setAsset(masterAsset);
        assetsRelation.setParentAsset(parentAsset);
        return assetsRelation;
    }
}
