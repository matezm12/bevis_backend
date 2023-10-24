package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.blockchaincore.domain.Blockchain;
import com.bevis.blockchaincore.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class BlockchainsController {
    private final BlockchainService blockchainService;

    @Secured({ADMIN, VENDOR})
    @ApiPageable
    @GetMapping("admin/blockchains")
    DataResponse<Blockchain> findAll(String search, Pageable pageable) {
        log.debug("REST to load blockchains...");
        return DataResponse.of(blockchainService.searchAll(search, pageable));
    }

    @Secured(ADMIN)
    @GetMapping("admin/blockchains/{id}")
    Blockchain findOne(@PathVariable Long id){
        log.debug("REST to load blockchains {}...", id);
        return blockchainService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("blockchain with id " + id + " not found "));
    }

    @Secured(ADMIN)
    @PostMapping("admin/blockchains")
    Blockchain create(@RequestBody @Valid Blockchain blockchain){
        log.debug("REST to create blockchain: {}", blockchain);
        return blockchainService.save(blockchain);
    }

    @Secured(ADMIN)
    @PutMapping("admin/blockchains")
    Blockchain update(@RequestBody @Valid Blockchain blockchain){
        log.debug("REST to update blockchain: {}", blockchain);
        if (Objects.isNull(blockchain.getId())){
            throw new RuntimeException("Id is null");
        }
        return blockchainService.save(blockchain);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/blockchains/{id}")
    void delete(@PathVariable Long id){
        log.debug("REST to delete blockchains by ID: {}", id);
        blockchainService.delete(id);
    }

}
