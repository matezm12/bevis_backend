package com.bevis.gateway;

public interface UrlGatewayService {
    String getIpfsUrl(String cid);

    String getAssetViewerLink(String assetId);

    String getGoogleMapLink(String gps);

    String getCarrierTrackingLink(String carrier, String trackingNumber);
}
