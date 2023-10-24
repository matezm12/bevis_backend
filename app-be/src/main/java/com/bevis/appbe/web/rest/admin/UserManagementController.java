package com.bevis.appbe.web.rest.admin;

import com.bevis.appbe.web.rest.annotation.ApiPageable;
import com.bevis.appbe.web.rest.vm.DataResponse;
import com.bevis.common.exception.ObjectNotFoundException;
import com.bevis.user.domain.User;
import com.bevis.user.UserMgmtService;
import com.bevis.user.dto.UserFilter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.bevis.security.AuthoritiesConstants.ADMIN;
import static com.bevis.security.AuthoritiesConstants.SUPERADMIN;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserManagementController {

    private final UserMgmtService userMgmtService;

    @Secured({SUPERADMIN, ADMIN})
    @GetMapping("admin/users/{id}")
    User findOne(@PathVariable Long id){
        log.debug("REST to load user {}...", id);
        return userMgmtService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("user with id " + id + " not found "));
    }

    @Secured({SUPERADMIN, ADMIN})
    @ApiPageable
    @GetMapping("admin/users")
    DataResponse<User> findAll(UserFilter filter, @NotNull Pageable pageable) {
        log.debug("REST to load users...");
        return DataResponse.of(userMgmtService.searchAll(filter, pageable));
    }

    @Secured({SUPERADMIN, ADMIN})
    @PostMapping("admin/users")
    public User createUser(@Valid @RequestBody User user) {
        log.debug("REST to create new user...");
        return userMgmtService.createUser(user);
    }

    @Secured({SUPERADMIN, ADMIN})
    @PutMapping("admin/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("REST to update new user...");
        return userMgmtService.updateUser(user);
    }

    @Secured({SUPERADMIN, ADMIN})
    @PutMapping("admin/users/activate")
    User activateUser(@RequestBody @Valid ActivateVm activateVm) {
        return userMgmtService.activateUser(activateVm.userId, activateVm.activated);
    }

    @Secured({SUPERADMIN, ADMIN})
    @GetMapping("admin/users/authorities")
    public List<String> getAuthorities() {
        return userMgmtService.getAuthorities();
    }

    @Data
    private static class ActivateVm{
        @NotNull
        Long userId;
        @NotNull
        boolean activated;
    }
}
