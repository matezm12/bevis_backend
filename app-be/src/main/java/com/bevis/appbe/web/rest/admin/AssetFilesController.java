package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.bevisassetpush.AssetFilePublisherService;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.filecore.domain.File;
import com.bevis.filecore.FileService;
import com.bevis.filecore.dto.FileFilter;
import com.bevis.master.MasterService;
import com.bevis.master.PermissionDeniedException;
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
import static com.bevis.security.util.SecurityUtils.checkPermissions;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AssetFilesController {

    private final FileService fileService;
    private final AssetFilePublisherService assetFilePublisherService;
    private final MasterService masterService;

    @Secured({ADMIN, VENDOR})
    @ApiPageable
    @GetMapping("admin/asset-files")
    DataResponse<File> findAll(FileFilter filter, Pageable pageable) {
        log.debug("REST to load files...");
        Page<File> page = fileService.searchFile(filter, pageable);
        return DataResponse.of(page);
    }

    @Secured({ADMIN, VENDOR})
    @GetMapping("admin/asset-files/{id}")
    File findOne(@PathVariable Long id) {
        log.debug("REST to load file by ID...");
        return fileService.findById(id)
                .map(x -> checkPermissions(x, x.getOwnerAssetId()))
                .orElseThrow(ObjectNotFoundException::new);
    }

    @Secured(ADMIN)
    @ApiPageable
    @PutMapping("admin/asset-files")
    File update(@RequestBody @Valid File file) {
        log.debug("REST to update file ...");
        if (Objects.isNull(file.getId())){
            throw new RuntimeException("Id is null");
        }
        return fileService.updateFileName(file);//for now only file-name allowed for editing
    }

    @Secured(ADMIN)
    @PostMapping("admin/asset-files/{id}/tx")
    File reTryTransactionManually(@PathVariable Long id) throws PermissionDeniedException {
        log.debug("REST to re-try tx ...");
        File fileDTO = fileService.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
        return assetFilePublisherService.reTryTransactionManually(fileDTO);
    }

    @Secured(ADMIN)
    @PutMapping("admin/asset-files/{id}/primary")
    File setPrimaryForAsset(@PathVariable Long id, boolean updateChildren) {
        log.debug("REST to set asset primary ...");
        File file = fileService.findById(id).orElseThrow(ObjectNotFoundException::new);
        masterService.updatePrimaryFile(file.getAssetId(), file, updateChildren);
        return file;
    }
}
