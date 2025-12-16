package br.com.appsemaperreio.escalante_api.seguranca.application.service;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.appsemaperreio.escalante_api.seguranca.application.LoginUseCases;
import br.com.appsemaperreio.escalante_api.seguranca.application.mappers.PerfilMapper;

@Service
public class LoginService implements LoginUseCases {

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
    public String login(Authentication authentication, String perfilString) {
        
        var principal = (UserDetails) authentication.getPrincipal();

        var passwordsPadroes = Set.of(senhaInicial, senhaPadrao);

        for (String pass : passwordsPadroes) {
            var invalido = pass == null || pass.isBlank();    
            if (!invalido && passwordEncoder.matches(pass, principal.getPassword()))
                return "";
        }
        
        var perfil = perfilMapper.stringToPerfil(perfilString);

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
