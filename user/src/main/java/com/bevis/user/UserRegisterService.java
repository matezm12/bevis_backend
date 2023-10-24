package com.bevis.user;

import com.bevis.user.domain.User;
import com.bevis.user.dto.UserRegister;

public interface UserRegisterService {

    User registerUser(UserRegister userRegister, String password, boolean activated);
}
