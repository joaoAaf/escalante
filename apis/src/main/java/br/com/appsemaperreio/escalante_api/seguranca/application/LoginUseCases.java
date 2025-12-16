package br.com.appsemaperreio.escalante_api.seguranca.application;

import org.springframework.security.core.Authentication;

public interface LoginUseCases {

    String login(Authentication authentication, String perfilString);

}
