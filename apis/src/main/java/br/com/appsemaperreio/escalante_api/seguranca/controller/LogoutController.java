package br.com.appsemaperreio.escalante_api.seguranca.controller;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.ILogoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    private final ILogoutService service;

    public LogoutController(ILogoutService logoutService) {
        this.service = logoutService;
    }

    @PostMapping
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        service.logout(jwt);
        return ResponseEntity.noContent().build();
    }
}
