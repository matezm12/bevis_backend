package com.bevis.config;

import com.bevis.config.domain.Config;
import com.bevis.config.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bevis.config.ConfigSpecification.bySearchQuery;

@Service
@Slf4j
@RequiredArgsConstructor
class ConfigManagementServiceImpl implements ConfigManagementService {

    private final ConfigRepository configRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<Config> findAll(Pageable pageable) {
        return configRepository.findAll(pageable);
    }

    @Override
    public Page<Config> searchAll(String search, Pageable pageable) {
        return configRepository.findAll(bySearchQuery(search), pageable);
    }

    @Override
    public Optional<Config> findByKey(String key) {
        return configRepository.findById(key);
    }

    @Override
    public Config save(Config config) {
        return configRepository.save(config);
    }
}
