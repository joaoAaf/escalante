package br.com.appsemaperreio.escalante_api.seguranca.model.application.service;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.ILoginService;
import br.com.appsemaperreio.escalante_api.seguranca.model.application.mappers.PerfilMapper;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginService implements ILoginService {

    private final JwtService jwtService;
    private final PerfilMapper perfilMapper;
    private final PasswordEncoder passwordEncoder;
    private final String senhaInicial;
    private final String senhaPadrao;

    public LoginService(
            JwtService jwtService,
            PerfilMapper perfilMapper,
            PasswordEncoder passwordEncoder,
            @Value("${env.usuario.inicial.password}") String senhaInicial,
            @Value("${env.usuario.padrao.password}") String senhaPadrao) {
        this.jwtService = jwtService;
        this.perfilMapper = perfilMapper;
        this.passwordEncoder = passwordEncoder;
        this.senhaInicial = senhaInicial;
        this.senhaPadrao = senhaPadrao;
    }

    @Override
    public String login(Authentication authentication, boolean loginAdmin) {
        
        var principal = (UserDetails) authentication.getPrincipal();

        var passwordsPadroes = Set.of(senhaInicial, senhaPadrao);

        for (String pass : passwordsPadroes) {
            var invalido = pass == null || pass.isBlank();
            if (!invalido && passwordEncoder.matches(pass, principal.getPassword()))
                return "";
        }

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
