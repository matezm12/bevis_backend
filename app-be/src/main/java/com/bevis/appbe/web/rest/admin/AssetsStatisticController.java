package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.asset.AssetsStatisticService;
import com.bevis.asset.dto.AssetStatisticDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AssetsStatisticController {

    private final AssetsStatisticService assetsStatisticService;

    @Secured(ADMIN)
    @GetMapping("admin/assets-statistic")
    DataResponse<AssetStatisticDTO> loadStatistic(String search, Pageable pageable) {
        return DataResponse.of(assetsStatisticService.loadStatistic(search, pageable));
    }
}
