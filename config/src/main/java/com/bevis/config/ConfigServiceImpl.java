package com.bevis.config;

import com.bevis.config.repository.ConfigRepository;
import com.bevis.common.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;

    @Override
    public String getIpfsLinkTemplate() {
        return getValue(ConfigKey.IPFS_LINK_TEMPLATE);
    }

    @Override
    public String getAssetViewerLinkTemplate() {
        return getValue(ConfigKey.ASSET_VIEWER_LINK_TEMPLATE);
    }

    @Override
    public String getValue(ConfigKey key) {
        return configRepository.findById(key.getValue())
                .orElseThrow(ObjectNotFoundException::new)
                .getValue();
    }

    @Override
    public String getValue(ConfigKey key, Object... args) {
        if (Objects.nonNull(args)) {
            return String.format(getValue(key), args);
        }
        return null;
    }
}
