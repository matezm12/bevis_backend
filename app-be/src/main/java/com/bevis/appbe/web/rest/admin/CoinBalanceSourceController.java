package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.balancecore.CoinBalanceSourceService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.balancecore.domain.CoinBalanceSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CoinBalanceSourceController {

    private final CoinBalanceSourceService coinBalanceSourceService;

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/coin-balance-sources")
    DataResponse<CoinBalanceSource> findAll(String search, Pageable pageable) {
        log.debug("REST to load coin-balance-sources...");
        return DataResponse.of(coinBalanceSourceService.searchAll(search, pageable));
    }

    @Secured(ADMIN)
    @GetMapping("admin/coin-balance-sources/{id}")
    CoinBalanceSource findOne(@PathVariable Long id){
        log.debug("REST to load coin-balance-sources {}...", id);
        return coinBalanceSourceService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("coin-balance-source with id " + id + " not found "));
    }

    @Secured(ADMIN)
    @PostMapping("admin/coin-balance-sources")
    CoinBalanceSource create(@RequestBody @Valid CoinBalanceSource coinBalanceSource){
        log.debug("REST to create coin-balance-source: {}", coinBalanceSource);
        return coinBalanceSourceService.save(coinBalanceSource);
    }

    @Secured(ADMIN)
    @PutMapping("admin/coin-balance-sources")
    CoinBalanceSource update(@RequestBody @Valid CoinBalanceSource coinBalanceSource){
        log.debug("REST to update coin-balance-sources: {}", coinBalanceSource);
        if (Objects.isNull(coinBalanceSource.getId())){
            throw new RuntimeException("Id is null");
        }
        return coinBalanceSourceService.save(coinBalanceSource);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/coin-balance-sources/{id}")
    void delete(@PathVariable Long id){
        log.debug("REST to delete coin-balance-sources by ID: {}", id);
        coinBalanceSourceService.deleteById(id);
    }
}
