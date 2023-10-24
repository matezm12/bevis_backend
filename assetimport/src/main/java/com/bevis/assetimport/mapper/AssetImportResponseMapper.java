package com.bevis.assetimport.mapper;

import com.bevis.assetimport.dto.AssetImportResponse;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.assetimport.repository.CodeReadrServiceRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class AssetImportResponseMapper {

    @Autowired
    private CodeReadrServiceRepository codeReadrServiceRepository;

    @Mapping(source = "sku.file", target = "file")
    public abstract AssetImportResponse toResponse(AssetImport assetImport);

    public AssetImportResponse mapResponse(AssetImport assetImport) {
        AssetImportResponse assetImportResponse = toResponse(assetImport);
        CodeReadrService service = codeReadrServiceRepository.findByServiceId(assetImportResponse.getServiceId())
               .orElse(null);
        if (Objects.nonNull(service)) {
            assetImportResponse.setServiceName(service.getServiceKey());
            assetImportResponse.setCodereadrServiceId(service.getId());
        }
        return assetImportResponse;
    }
}
