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

    private final ILoginService service;

    public LoginController(ILoginService loginUseCases) {
        this.service = loginUseCases;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> login(
            Authentication authentication,
            @RequestParam(value = "admin", defaultValue = "false")
            boolean loginAdmin) {
        return ResponseEntity.ok(Map.of("bearerToken", service.login(authentication, loginAdmin)));
    }

}
