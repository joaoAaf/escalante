package apisemaperreio.escalante.escalante.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.escalante.services.loginService;

@RestController
@RequestMapping("/api/login")
public class loginController {

    private final loginService loginService;

    public loginController(loginService autenticacaoService) {
        this.loginService = autenticacaoService;
    }

    @PostMapping
    public String login(Authentication authentication) {
        return loginService.login(authentication);
    }

}
