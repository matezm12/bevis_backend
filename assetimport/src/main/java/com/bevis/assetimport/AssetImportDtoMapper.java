package com.bevis.assetimport;

import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.domain.AssetImport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AssetImportDtoMapper {

    @Mapping(source = "barcode", target = "barcodeItems", qualifiedByName = "fromBarcode")
    @Mapping(source = "barcode", target = "barcode")
    @Mapping(source = "scanDate", target = "scanTime")
    @Mapping(source = "uploadDate", target = "uploadTime")
    AssetImportDTO toDto(AssetImport assetImport);

    @Named("fromBarcode")
    default List<String> fromBarcode(String barcode) {
        String[] barcodeData = barcode.split("\n");
        return Arrays.stream(barcodeData).map(x -> x.replace("\r", "")).collect(Collectors.toList());
    }
}
