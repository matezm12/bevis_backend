package com.bevis.assetinfo;

import com.bevis.assetinfo.dto.ProductInfoDTO;

public interface ProductInfoService {
    ProductInfoDTO getById(String assetId);
}
