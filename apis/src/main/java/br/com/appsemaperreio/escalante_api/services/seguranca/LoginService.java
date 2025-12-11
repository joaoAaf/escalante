package br.com.appsemaperreio.escalante_api.services.seguranca;

import java.util.Collections;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;

@Service
public class LoginService {

    private final JwtService jwtService;

    public LoginService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String login(Authentication authentication, String perfilString) {
        Perfil perfil;
        try {
            perfil = Perfil.valueOf(perfilString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Perfil inválido: " + perfilString);
        }

        var authority = authentication.getAuthorities().stream()
                .filter(auth -> auth.getAuthority().equals("ROLE_" + perfil.name()))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Acesso negado para o perfil: " + perfil.name()));

        var newAuthentication = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                null,
                Collections.singletonList(
                        new SimpleGrantedAuthority(authority.getAuthority())));
        
        return jwtService.gerarToken(newAuthentication);
    }
}
