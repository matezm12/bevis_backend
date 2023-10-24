package com.bevis.asset;

import com.bevis.master.domain.Master;
import com.bevis.asset.dto.AssetDTO;
import com.bevis.asset.dto.AssetRequest;
import com.bevis.asset.dto.FieldValueDTO;
import com.bevis.master.dto.SearchMasterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DynamicAssetService {

    AssetDTO getById(String assetId);

    AssetDTO getByMaster(Master master);

    Optional<AssetDTO> findOne(SearchMasterRequest params);

    List<AssetDTO> findAll(SearchMasterRequest params);

    Page<AssetDTO> findAll(SearchMasterRequest params, Pageable pageable);

    Map<String, FieldValueDTO> getDynamicFieldsById(String assetId);

    AssetDTO create(AssetRequest assetRequest) throws DynamicAssetException;

    AssetDTO update(AssetRequest assetRequest) throws DynamicAssetException;

    void deleteByMaster(Master master) throws DynamicAssetException;
}
