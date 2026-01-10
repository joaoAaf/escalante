package br.com.appsemaperreio.escalante_api.seguranca.model.application;

import org.springframework.security.oauth2.jwt.Jwt;

public interface ILogoutService {

    void logout(Jwt jwt);

}
