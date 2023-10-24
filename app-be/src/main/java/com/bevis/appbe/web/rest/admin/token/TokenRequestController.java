package com.bevis.appbe.web.rest.admin.token;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.nftcore.domain.TokenRequest;
import com.bevis.nftcore.tokenrequest.CreateTokenRequestService;
import com.bevis.nftcore.tokenrequest.TokenRequestDetailsService;
import com.bevis.nftcore.tokenrequest.TokenRequestService;
import com.bevis.nftcore.tokenrequest.TokenTransferService;
import com.bevis.nftcore.tokenrequest.dto.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenRequestController {

    private final CreateTokenRequestService createTokenRequestService;
    private final TokenTransferService tokenTransferService;
    private final TokenRequestService tokenRequestService;
    private final TokenRequestDetailsService tokenRequestDetailsService;

    @ApiOperation(value = "Create new TokenRequest...")
    @Secured(ADMIN)
    @PostMapping("admin/token-requests")
    TokenRequestDetailsDTO createTokenRequest(@RequestBody @Valid CreateTokenRequestDTO request) {
        log.debug("REST to create new TokenRequest...");
        return createTokenRequestService.createTokenRequest(request);
    }

    @ApiOperation(value = "Retry failed transfers for TokenRequest...")
    @Secured(ADMIN)
    @PutMapping("admin/token-requests/{id}/retry")
    TokenRequestDetailsDTO retryFailedTransfers(@PathVariable Long id) {
        log.debug("REST to retry failed transfers for TokenRequest...");
        return createTokenRequestService.retryFailedTransfers(id);
    }

    @ApiOperation(value = "Loading list of TokenRequests...")
    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/token-requests")
    DataResponse<TokenRequest> loadTokenRequests(SearchDTO search, Pageable pageable) {
        log.debug("REST to get list TokenRequest...");
        return DataResponse.of(tokenRequestService.findAll(search, pageable));
    }

    @ApiOperation(value = "Get TokenRequest details...")
    @Secured(ADMIN)
    @GetMapping("admin/token-requests/{id}")
    TokenRequestDetailsDTO getTokenRequest(@PathVariable Long id) {
        log.debug("REST to get TokenRequest details...");
        return tokenRequestDetailsService.getDetails(id);
    }

    @ApiOperation(value = "Get TokenTransfers of current TokenRequest...")
    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/token-requests/{id}/transfers")
    DataResponse<TokenTransferDTO> getTokenTransfers(@PathVariable Long id, TokenTransferFilterDTO filter, Pageable pageable){
        log.debug("REST to get TokenTransfers of current TokenRequest...");
        filter.setRequestId(id);
        return DataResponse.of(tokenTransferService.findAll(filter, pageable));
    }
}
