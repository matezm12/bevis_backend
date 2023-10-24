package com.bevis.appbe.web.rest.bevisapp;

import com.bevis.user.domain.User;
import com.bevis.lister.ListerAssetService;
import com.bevis.lister.dto.AssetNameRequest;
import com.bevis.lister.dto.ListerAssetRequest;
import com.bevis.lister.dto.ListerAssetResponse;
import com.bevis.user.UserService;
import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.USER;

@Api(description = "API for manage user's assets scanned by READ function or added by himself")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ListerAssetController {

    private final ListerAssetService listerAssetService;
    private final UserService userService;

    @ApiOperation(value = "Get user's assets list")
    @Secured({ADMIN, USER})
    @ApiPageable
    @GetMapping("/lister-assets")
    DataResponse<ListerAssetResponse> getCurrentUserList(Pageable pageable, @RequestParam(value = "display-currency", required = false) String displayCurrency) {
        log.debug("getAllByCurrentUser");
        final User currentUser = userService.getCurrentUser();
        return DataResponse.of(listerAssetService.getUserList(currentUser, pageable, displayCurrency));
    }

    @ApiOperation(value = "Add new asset to user's list")
    @Secured({ADMIN, USER})
    @PostMapping("/lister-assets")
    ListerAssetResponse addToUserList(@RequestBody @Valid ListerAssetRequest assetRequest) {
        log.debug("addToUserList");
        final User currentUser = userService.getCurrentUser();
        return listerAssetService.addToUserList(assetRequest, currentUser);
    }

    @ApiOperation(value = "Remove asset from user's list")
    @Secured({ADMIN, USER})
    @DeleteMapping("/lister-assets/{asset-id}")
    void removeFromUserList(@PathVariable("asset-id") String assetId) {
        log.debug("removeFromUserList");
        final User currentUser = userService.getCurrentUser();
        listerAssetService.removeFromUserList(assetId, currentUser);
    }

    @ApiOperation(value = "Rename asset in user's list")
    @Secured({ADMIN, USER})
    @PutMapping("/lister-assets/{asset-id}")
    ListerAssetResponse renameForUser(@PathVariable("asset-id") String assetId, @RequestBody @Valid AssetNameRequest assetNameRequest) {
        final User currentUser = userService.getCurrentUser();
        return listerAssetService.renameForUser(assetId, assetNameRequest.getName(), currentUser);
    }

}
