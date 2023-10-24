package com.bevis.bevisassetpush.mapper;

import com.bevis.bevisassetpush.dto.BevisAssetDTO;
import com.bevis.master.domain.Master;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AssetMapper {
    @Mapping(source = "id", target = "assetId")
    @Mapping(source = "blockchain.name", target = "blockchain")
    BevisAssetDTO toDto(Master master);
}
