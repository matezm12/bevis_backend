package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.config.ConfigManagementService;
import com.bevis.config.domain.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConfigController {

    private final ConfigManagementService configManagementService;

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/configs")
    DataResponse<Config> findAll(String search, Pageable pageable) {
        log.debug("REST to load configs...");
        return DataResponse.of(configManagementService.searchAll(search, pageable));
    }

    @Secured(ADMIN)
    @GetMapping("admin/configs/{key}")
    Config getOne(@PathVariable String key) {
        log.debug("REST to load config by key...");
        return configManagementService.findByKey(key)
                .orElseThrow(() -> new ObjectNotFoundException("config with key " + key + " not found "));
    }

    @Secured(ADMIN)
    @PutMapping("admin/configs")
    Config update(@RequestBody @Valid Config config){
        log.debug("REST to update config: {}", config);
        if (Objects.isNull(config.getKey())){
            throw new RuntimeException("Key is null");
        }
        return configManagementService.save(config);
    }
}
