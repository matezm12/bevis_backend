package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.user.domain.User;
import com.bevis.lister.ListerAssetService;
import com.bevis.lister.dto.ListerAssetRequest;
import com.bevis.lister.dto.ListerAssetResponse;
import com.bevis.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@Api(description = "API for manage user's assets scanned by READ function or added by himself")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminListerAssetController {

    private final ListerAssetService listerAssetService;
    private final UserService userService;

    @ApiOperation(value = "Get user's assets list")
    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("/admin/lister-assets")
    DataResponse<ListerAssetResponse> getCurrentUserList(String search, Pageable pageable, @RequestParam(value = "display-currency", required = false) String displayCurrency) {
        log.debug("getAllByCurrentUser");
        final User currentUser = userService.getCurrentUser();
        return DataResponse.of(listerAssetService.getUserList(currentUser, search, pageable, displayCurrency));
    }

    @ApiOperation(value = "Get Asset by user")
    @Secured(ADMIN)
    @GetMapping("/admin/lister-assets/{asset-id}")
    ListerAssetResponse findOne(@PathVariable("asset-id") String assetId) {
        log.debug("findOne");
        final User currentUser = userService.getCurrentUser();
        return listerAssetService.findOne(assetId, currentUser);
    }

    @ApiOperation(value = "Add new asset to user's list")
    @Secured(ADMIN)
    @PostMapping("/admin/lister-assets")
    ListerAssetResponse addToUserList(@RequestBody @Valid ListerAssetRequest assetRequest) {
        log.debug("addToUserList");
        final User currentUser = userService.getCurrentUser();
        return listerAssetService.addToUserList(assetRequest, currentUser);
    }

    @ApiOperation(value = "Remove asset from user's list")
    @Secured(ADMIN)
    @DeleteMapping("/admin/lister-assets/{asset-id}")
    void removeFromUserList(@PathVariable("asset-id") String assetId) {
        log.debug("removeFromUserList");
        final User currentUser = userService.getCurrentUser();
        listerAssetService.removeFromUserList(assetId, currentUser);
    }

    @ApiOperation(value = "Rename asset in user's list")
    @Secured(ADMIN)
    @PutMapping("/admin/lister-assets")
    ListerAssetResponse renameForUser(@RequestBody @Valid ListerAssetRequest assetNameRequest) {
        final User currentUser = userService.getCurrentUser();
        return listerAssetService.renameForUser(assetNameRequest.getAssetId(), assetNameRequest.getName(), currentUser);
    }

}
