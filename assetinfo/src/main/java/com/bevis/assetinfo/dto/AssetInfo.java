package com.bevis.assetinfo.dto;

import com.bevis.assettype.domain.AssetType;
import com.bevis.coininfo.dto.CoinInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetInfo {

    private AssetType assetType;

    private VendorDTO vendor;

    private List<AssetValue> displayValues;

    private CoinInfo coinInfo;

    private MasterData master;

    private ProductData product;

    private LocationInfo location;

    private ShipmentInfo shipment;


}
