package com.bevis.appbe.web.rest.admin;


import com.amazonaws.util.StringInputStream;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.master.MasterImportService;
import com.bevis.master.dto.AssetKeyCsvDTO;
import com.bevis.master.dto.MasterImportCsvDataDTO;
import com.bevis.master.dto.MasterImportDTO;
import com.bevis.master.dto.MasterImportRequest;
import com.bevis.master.mapper.MasterImportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Objects;

import static com.bevis.appbe.web.rest.util.DownloadUtil.getDownloadFileResponse;
import static com.bevis.csv.CsvGenerator.generateCsvStringFromList;
import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MasterImportController {

    private final MasterImportService masterImportService;
    private final MasterImportMapper masterImportMapper;

    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("/admin/master-imports")
    DataResponse<MasterImportDTO> findAll(@RequestParam(name = "search", required = false) String search,
                                          @NotNull Pageable pageable) {
        log.debug("REST request to get all Masters");
        return DataResponse
                .of(Objects.isNull(search) ? masterImportService.findAll(pageable) :
                        masterImportService.findAllByNameContaining(search, pageable))
                .map(masterImportMapper::toDto);
    }

    @Secured(ADMIN)
    @GetMapping("/admin/master-imports/{id}")
    MasterImportDTO findOne(@PathVariable Long id) {
        return masterImportMapper.toDto(masterImportService.getOne(id));
    }

    @Secured(ADMIN)
    @PostMapping("/admin/master-imports")
    MasterImportDTO generateMasters(@RequestBody @Valid MasterImportRequest masterGroupRequest) {
        log.debug("REST to import master records from CSV");
        return masterImportService.generateMasters(masterGroupRequest);
    }

    @Secured(ADMIN)
    @PutMapping("/admin/master-imports")
    MasterImportDTO updateMasters(@RequestBody @Valid MasterImportRequest masterGroupRequest) {
        log.debug("REST to import master records from CSV");
        return masterImportService.updateMasters(masterGroupRequest);
    }

    @Secured(ADMIN)
    @GetMapping("/admin/master-imports/{masterImportId}/keys/csv")
    @CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition", "X-Suggested-Filename"})
    ResponseEntity<Resource> downloadMasterImportCsv(@PathVariable Long masterImportId) {
        final MasterImportCsvDataDTO masterImportCsvDataDTO = masterImportService.getMasterImportAssetKeys(masterImportId);
        String csvFileData = generateCsvStringFromList(masterImportCsvDataDTO.getAssetKeys(), AssetKeyCsvDTO.FIELDS_ORDER);
        try (InputStream inputStream = new StringInputStream(csvFileData)) {
            return getDownloadFileResponse(inputStream, masterImportCsvDataDTO.getFileName(), csvFileData.getBytes().length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

}
