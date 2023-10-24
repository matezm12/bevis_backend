package com.bevis.assetimport.util;

import com.bevis.assetimport.dto.ProductDTO;
import com.bevis.assettype.domain.AssetType;
import com.bevis.master.domain.Master;

import java.util.Objects;

public final class DynamicProductMapper {

    public static ProductDTO mapProductDto(Master master) {
        AssetType assetType = Objects.nonNull(master) ? master.getAssetType() : null;
        return new ProductDTO(master.getId(), assetType);
    }
}
