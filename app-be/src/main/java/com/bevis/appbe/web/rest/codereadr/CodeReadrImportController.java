package com.bevis.appbe.web.rest.codereadr;

import com.bevis.assetimport.codereadr.CodeReadrImportService;
import com.bevis.assetimport.dto.CodeReadrResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(description="Importing assets from CodeReadr resources")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CodeReadrImportController {

    private final CodeReadrImportService codeReadrImportService;

    @ApiOperation(value = "Endpoint used only for CodeReadr App")
    @ResponseBody
    @PostMapping(value = "code-readr/verify-scan-64c8cdbf", produces = {MediaType.APPLICATION_XML_VALUE})
    CodeReadrResponse importFromCodeReadrApp(@RequestBody String body, @RequestParam("api-key") String apiKey){
        return codeReadrImportService.importAssetsFromCodereadrBody(body, apiKey);
    }

    @ApiOperation(value = "Endpoint used only for Rearden CodeReadr App")
    @ResponseBody
    @PostMapping(value = "code-readr/rearden/verify-scan-64c8cdbf", produces = {MediaType.APPLICATION_XML_VALUE})
    CodeReadrResponse importFromCodeReadrReardenApp(@RequestBody String body, @RequestParam("api-key") String apiKey){
        return codeReadrImportService.importAssetsFromCodereadrBody(body, apiKey);
    }
}
