package com.bevis.assetinfo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductData {
    private String sku;
    private String img;
    private List<String> images;
    private String imgBack;
}
