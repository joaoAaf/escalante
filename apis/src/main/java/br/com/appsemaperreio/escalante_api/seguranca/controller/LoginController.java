package br.com.appsemaperreio.escalante_api.seguranca.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.ILoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final ILoginService loginUseCases;

    public LoginController(ILoginService loginUseCases) {
        this.loginUseCases = loginUseCases;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> login(Authentication authentication, @RequestParam String perfil) {
        return ResponseEntity.ok(Map.of("bearerToken", loginUseCases.login(authentication, perfil)));
    }

}
