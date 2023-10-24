package com.bevis.config;

public enum ConfigKey {

    CARRIER_TRACKING_LINK_TEMPLATE("CARRIER_TRACKING_LINK_TEMPLATE"),
    IPFS_LINK_TEMPLATE("IPFS_LINK_TEMPLATE"),
    ASSET_VIEWER_LINK_TEMPLATE("ASSET_VIEWER_LINK_TEMPLATE"),
    GOOGLE_MAP_GPS_LINK_TEMPLATE("GOOGLE_MAP_GPS_LINK_TEMPLATE"),
    CODEREADR_NOTIFICATION_RECEIVERS("CODEREADR_NOTIFICATION_RECEIVERS"),
    DEFAULT_BLOCKCHAIN("DEFAULT_BLOCKCHAIN");

    private final String value;

    ConfigKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
