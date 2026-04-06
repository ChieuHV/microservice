package com.demosecurity.service;

import com.demosecurity.dto.UserLogInDTO;
import com.demosecurity.dto.UserRegisterDTO;

public interface UserService {
    String register(UserRegisterDTO userRegisterDTO);

    String login(UserLogInDTO userLogInDTO);
}
