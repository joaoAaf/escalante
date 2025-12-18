package br.com.appsemaperreio.escalante_api.seguranca.model.application;

import org.springframework.security.core.Authentication;

public interface ILoginService {

    String login(Authentication authentication, String perfilString);

}
