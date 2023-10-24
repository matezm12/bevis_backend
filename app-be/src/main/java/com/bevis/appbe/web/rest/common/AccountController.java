package com.bevis.appbe.web.rest.common;

import com.bevis.account.AccountService;
import com.bevis.account.dto.Account;
import com.bevis.appbe.web.rest.vm.*;
import com.bevis.master.PermissionDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST register new account");
        accountService.registerAccount(managedUserVM, managedUserVM.getPassword());
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key the activation key
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    void activateAccount(@RequestParam String key) {
        log.debug("REST to activate account");
        accountService.activateAccount(key);
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }


    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    Account getAccount() {
        log.debug("REST request account info");
        return accountService.getAccount();
    }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordVM the new password
     */
    @PostMapping("/account/change-password")
    @ResponseStatus(HttpStatus.OK)
    void changePassword(@RequestBody @Valid PasswordVM passwordVM) {
        log.debug("REST change password for  account");
        accountService.changePassword(passwordVM.getCurrentPassword(), passwordVM.getNewPassword());
    }

    /**
     * POST   /account/reset-password/init : Send an email to reset the password of the user
     *
     * @param emailVm the mail of the user
     */
    @PostMapping("/account/reset-password/init")
    @ResponseStatus(HttpStatus.OK)
    void requestPasswordReset(@RequestBody EmailVm emailVm) {
        log.debug("REST reset password init for  account");
        accountService.requestPasswordReset(emailVm.getEmail());
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword the generated key and the new password
     * @throws RuntimeException 500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping("/account/reset-password/finish")
    @ResponseStatus(HttpStatus.CREATED)
    void finishPasswordReset(@RequestBody @Valid KeyAndPasswordVM keyAndPassword) {
        log.debug("REST reset password finish for  account");
        accountService.finishPasswordReset(keyAndPassword.getKey(), keyAndPassword.getNewPassword());
    }

    /**
     * DELETE  /account :  DELETE the current user.
     */
    @DeleteMapping("/account")
    void deleteAccount(@RequestHeader String password) throws PermissionDeniedException {
        log.debug("REST request to delete account");
        accountService.deleteAccount(password);
    }
}
