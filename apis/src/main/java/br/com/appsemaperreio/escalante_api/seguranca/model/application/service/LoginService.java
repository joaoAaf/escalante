package br.com.appsemaperreio.escalante_api.seguranca.model.application.service;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.ILoginService;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.UsuarioAutenticado;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class LoginService implements ILoginService {

    private final JwtService jwtService;

    public LoginService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String login(Authentication authentication, boolean loginAdmin) {

        var usuarioAuth = (UsuarioAutenticado) authentication.getPrincipal();

        if (usuarioAuth.isSenhaTemporaria()) return "";

        var ehAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + Perfil.ADMIN.name()));

        if (!ehAdmin || authentication.getAuthorities().size() == 1) return jwtService.gerarToken(authentication);

        if (loginAdmin) {
            var authority = new SimpleGrantedAuthority("ROLE_" + Perfil.ADMIN.name());
            var newAuthentication = new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    Collections.singletonList(authority)
            );

            return jwtService.gerarToken(newAuthentication);
        }

        var authorities = authentication.getAuthorities().stream()
                .filter(auth -> !auth.getAuthority().equals("ROLE_" + Perfil.ADMIN.name()))
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                .collect(Collectors.toSet());

        var newAuthentication = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                null,
                authorities
        );

        return jwtService.gerarToken(newAuthentication);
    }
}
