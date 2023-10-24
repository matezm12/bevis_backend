package com.bevis.assettype;

import com.bevis.assettype.domain.AssetType;
import com.bevis.common.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class AssetTypesServiceImpl implements AssetTypesService {

    private final AssetTypesRepository assetTypesRepository;

    @Transactional(readOnly = true)
    @Override
    public AssetType getBevisAssetType() {
        String BEVIS_ASSET_TYPE_KEY = "bevis_assets";
        return assetTypesRepository.findOneByKey(BEVIS_ASSET_TYPE_KEY)
                .orElseThrow(ObjectNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AssetType> findById(Long assetTypeId) {
        return assetTypesRepository.findById(assetTypeId);
    }

    @Override
    public Map<String, Object> getAssetsTypeFieldsByTypeId(Long assetTypeId) {
        return findById(assetTypeId)
                .map(AssetType::getFieldsSchema)
                .orElse(new HashMap<>());
    }

    @Override
    public List<AssetType> getProductAssetTypes() {
        return assetTypesRepository.findAllByIsProductTrue();
    }

    @Override
    public Optional<AssetType> findFirstByKey(String key) {
        return assetTypesRepository.findFirstByKey(key);
    }

}
