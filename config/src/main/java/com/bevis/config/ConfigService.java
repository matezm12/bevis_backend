package com.bevis.config;

public interface ConfigService {
    String getValue(ConfigKey key);
    String getValue(ConfigKey key, Object... args);
    String getIpfsLinkTemplate();
    String getAssetViewerLinkTemplate();
}
