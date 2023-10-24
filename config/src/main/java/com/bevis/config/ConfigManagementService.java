package com.bevis.config;

import com.bevis.config.domain.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ConfigManagementService {

    Page<Config> findAll(Pageable pageable);

    Page<Config> searchAll(String search, Pageable pageable);

    Optional<Config> findByKey(String key);

    Config save(Config config);
}
