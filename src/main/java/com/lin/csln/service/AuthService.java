package com.lin.csln.service;

import com.lin.csln.dto.login.LoginReqDTO;
import com.lin.csln.dto.login.LoginRespDTO;

public interface AuthService {

    LoginRespDTO login(LoginReqDTO loginRequest);

    LoginRespDTO refresh(String refreshToken);

    void logout(String accessToken);
}
