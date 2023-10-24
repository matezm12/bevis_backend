package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.assetimport.CodeReadrMgmtService;
import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.common.exception.ObjectNotFoundException;
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
public class CodeReadrServiceController {
    private final CodeReadrMgmtService codeReadrMgmtService;

    @Secured({ADMIN, VENDOR})
    @ApiPageable
    @GetMapping("admin/code-readr-services")
    DataResponse<CodeReadrService> findAll(String search, Pageable pageable) {
        log.debug("REST to load codereadr-service...");
        return DataResponse.of(codeReadrMgmtService.searchAll(search, pageable));
    }

    @Secured(ADMIN)
    @GetMapping("admin/code-readr-services/{id}")
    CodeReadrService findOne(@PathVariable Long id){
        log.debug("REST to load codereadr-service {}...", id);
        return codeReadrMgmtService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("codereadr-service with id " + id + " not found "));
    }

    @Secured(ADMIN)
    @PostMapping("admin/code-readr-services")
    CodeReadrService create(@RequestBody @Valid CodeReadrService codeReadrService){
        log.debug("REST to create codereadr-service: {}", codeReadrService);
        return codeReadrMgmtService.save(codeReadrService);
    }

    @Secured(ADMIN)
    @PutMapping("admin/code-readr-services")
    CodeReadrService update(@RequestBody @Valid CodeReadrService codeReadrService){
        log.debug("REST to update codereadr-service: {}", codeReadrService);
        if (Objects.isNull(codeReadrService.getId())){
            throw new RuntimeException("Id is null");
        }
        return codeReadrMgmtService.save(codeReadrService);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/code-readr-services/{id}")
    void delete(@PathVariable Long id){
        log.debug("REST to delete codereadr-service by ID: {}", id);
        codeReadrMgmtService.deleteById(id);
    }
}
