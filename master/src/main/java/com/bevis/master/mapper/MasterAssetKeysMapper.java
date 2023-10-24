package com.bevis.master.mapper;

import com.bevis.master.domain.Master;
import com.bevis.master.dto.AssetKeyCsvDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MasterAssetKeysMapper {

    @Mapping(source = "id", target = "assetId")
    @Mapping(source = "publicKey", target = "publicKey")
    AssetKeyCsvDTO toDto(Master master);

}
