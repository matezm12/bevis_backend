package com.bevis.appbe.web.rest.admin;

import com.bevis.filecode.domain.FileCode;
import com.bevis.filecode.FileCodesManagementService;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.common.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class FileCodesController {

    private final FileCodesManagementService fileCodesManagementService;

    @Secured({ADMIN, VENDOR})
    @ApiPageable
    @GetMapping("admin/file-codes")
    DataResponse<FileCode> findAll(String search, Pageable pageable) {
        log.debug("REST to load file-codes...");
        return DataResponse.of(fileCodesManagementService.searchAll(search, pageable));
    }

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/file-codes/{id}")
    FileCode findOne(@PathVariable String id){
        log.debug("REST to load file-codes {}...", id);
        return fileCodesManagementService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("file-code with id " + id + " not found "));
    }


    @Secured(ADMIN)
    @PostMapping("admin/file-codes")
    FileCode create(@RequestBody @Valid FileCode fileCode){
        log.debug("REST to create file-codes: {}", fileCode);
        return fileCodesManagementService.save(fileCode);
    }

    @Secured(ADMIN)
    @PutMapping("admin/file-codes")
    FileCode update(@RequestBody @Valid FileCode fileCode){
        log.debug("REST to update file-codes: {}", fileCode);
        if (Objects.isNull(fileCode.getFileType())){
            throw new RuntimeException("Id is null");
        }
        return fileCodesManagementService.save(fileCode);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/file-codes/{id}")
    void delete(@PathVariable String id){
        log.debug("REST to delete file-codes by ID: {}", id);
        fileCodesManagementService.delete(id);
    }
}
