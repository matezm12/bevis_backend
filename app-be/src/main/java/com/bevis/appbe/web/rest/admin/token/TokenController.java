package com.bevis.appbe.web.rest.admin.token;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.nftcore.domain.Token;
import com.bevis.nftcore.tokenrequest.TokenService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {
    private final TokenService tokenService;

    @ApiOperation(value = "Loading list of Tokens...")
    @Secured(ADMIN)
    @ApiPageable
    @GetMapping("admin/tokens")
    DataResponse<Token> findAll(String search, Pageable pageable) {
        log.debug("REST to get list of Tokens...");
        return DataResponse.of(tokenService.findAll(search, pageable));
    }

    @Secured(ADMIN)
    @GetMapping("admin/tokens/{id}")
    Token findOne(@PathVariable Long id) {
        log.debug("REST to get Token...");
        return tokenService.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
    }

    @Secured(ADMIN)
    @PostMapping("admin/tokens")
    Token create(@RequestBody @Valid Token token) {
        log.debug("Rest to create token...");
        return tokenService.create(token);
    }

    @Secured(ADMIN)
    @PutMapping("admin/tokens")
    Token update(@RequestBody @Valid Token token) {
        log.debug("Rest to update token...");
        if (Objects.isNull(token.getId())) {
            throw new RuntimeException("Id is null");
        }
        return tokenService.update(token);
    }

    @Secured(ADMIN)
    @DeleteMapping("admin/tokens/{id}")
    void delete(@PathVariable Long id) {
        log.debug("Rest to delete token...");
        tokenService.deleteById(id);
    }

}
