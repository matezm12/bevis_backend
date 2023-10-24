package com.bevis.coininfo.mapper;

import com.bevis.assetinfo.dto.AssetValue;
import com.bevis.coininfo.dto.CoinDetailsItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bevis.assetinfo.mapper.AssetInfoMapper.BLACKLISTED_FIELDS;

@Component
@RequiredArgsConstructor
public class CoinDetailsMapper {

    public List<CoinDetailsItem> mapDynamicValues(List<AssetValue> assetValues) {
        return assetValues.stream()
                .filter(x -> Objects.nonNull(x.getFieldValue()))
                .filter(x -> !BLACKLISTED_FIELDS.contains(x.getFieldName()))
                .map(this::mapDynamicValue)
                .collect(Collectors.toList());
    }

    private CoinDetailsItem mapDynamicValue(AssetValue assetValue) {
        return CoinDetailsItem.builder()
                .fieldTitle(assetValue.getFieldTitle())
                .fieldValue(assetValue.getFieldValue())
                .isAsset(assetValue.isAsset())
                .link(assetValue.getLink())
                .build();
    }
}
