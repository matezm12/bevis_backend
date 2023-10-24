package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.AssetImportService;
import com.bevis.assetimport.codereadr.CsvCodereadrImportService;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.dto.AssetImportFilter;
import com.bevis.assetimport.dto.AssetImportResponse;
import com.bevis.assetimport.mapper.AssetImportResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.VENDOR;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AssetImportController {

    private final AssetImportService assetImportService;
    private final AssetImportResponseMapper assetImportResponseMapper;
    private final CsvCodereadrImportService csvCodereadrImportService;

    @Secured({ ADMIN, VENDOR })
    @ApiPageable
    @GetMapping("admin/asset-imports")
    DataResponse<AssetImportResponse> findAll(AssetImportFilter filter,
                                              @NotNull Pageable pageable) {
        log.debug("REST request to get all AssetImports");
        Page<AssetImport> assetImportPage;
        assetImportPage = assetImportService.findAll(filter, pageable);
        return DataResponse.of(assetImportPage.map(assetImportResponseMapper::mapResponse));

    }

    @Secured({ ADMIN, VENDOR })
    @GetMapping("admin/asset-imports/{id}")
    AssetImport findOne(@PathVariable Long id) {
        return assetImportService.findOne(id);
    }

    @Secured(ADMIN)
    @PutMapping("admin/asset-imports/re-import")
    AssetImport reImportAssets(@RequestBody AssetImport assetImport) throws AssetImportException {
        log.debug("Rest to reImportAssets: {}", assetImport);
        return assetImportService.reImportAssets(assetImport);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/asset-imports/{id}/undo")
    AssetImport undoAssetImport(@PathVariable Long id) throws AssetImportException {
        log.debug("REST to undo asset-import: {}", id);
        return assetImportService.undoAssetImport(id);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/asset-imports/{id}")
    void deleteAssetImport(@PathVariable Long id) throws AssetImportException {
        log.debug("REST to delete asset-import: {}", id);
        assetImportService.deleteById(id);
    }

    @Secured(ADMIN)
    @PostMapping("code-readr/csv")
    void importFromCsv(MultipartFile file) {
        csvCodereadrImportService.importFromCsv(file);
    }
}
