package br.com.appsemaperreio.escalante_api.controllers.seguranca;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.appsemaperreio.escalante_api.services.seguranca.LoginService;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService autenticacaoService) {
        this.loginService = autenticacaoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>>login(Authentication authentication) {
        return ResponseEntity.ok(Map.of("bearerToken", loginService.login(authentication)));
    }

}
