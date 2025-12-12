package br.com.appsemaperreio.escalante_api.application.seguranca;

import org.springframework.security.core.Authentication;

public interface LoginUseCases {

    String login(Authentication authentication, String perfilString);

}
