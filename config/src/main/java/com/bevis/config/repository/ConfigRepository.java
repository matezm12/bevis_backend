package com.bevis.config.repository;

import com.bevis.config.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConfigRepository extends JpaRepository<Config, String>, JpaSpecificationExecutor<Config> {
}
