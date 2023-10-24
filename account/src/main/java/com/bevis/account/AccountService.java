package com.bevis.account;

import com.bevis.account.dto.Account;
import com.bevis.master.PermissionDeniedException;
import com.bevis.user.domain.User;
import com.bevis.user.dto.UserRegister;

public interface AccountService {
    User registerAccount(UserRegister userRegister, String password);

    void activateAccount(String key);

    Account getAccount();

    void changePassword(String currentPassword, String newPassword);

    void requestPasswordReset(String email);

    void finishPasswordReset(String key, String newPassword);

    void deleteAccount(String password) throws PermissionDeniedException;
}
