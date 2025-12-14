package br.com.appsemaperreio.escalante_api.application.seguranca.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final String emissor;
    private final long tempoExpiracao;

    public JwtService(
            JwtEncoder encoder,
            JwtDecoder decoder,
            @Value("${spring.application.name}") String emissor,
            @Value("${env.jwt.expiration}") long tempoExpiracao) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.emissor = emissor;
        this.tempoExpiracao = tempoExpiracao;
    }

    public String gerarToken(Authentication authentication) {
        Instant instanteAtual = Instant.now();

        var perfis = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.startsWith("ROLE_") ? authority.substring(5) : authority)
                .toList();

        var claims = JwtClaimsSet.builder()
                .issuer(emissor)
                .issuedAt(instanteAtual)
                .expiresAt(instanteAtual.plusSeconds(tempoExpiracao))
                .subject(authentication.getName())
                .claim("perfis", perfis)
                .build();

        // Cabeçalho do JWT especificando o algoritmo de assinatura
        var jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public String extrairUsername(String token) {
        var jwt = decoder.decode(token);
        return jwt.getSubject();
    }

}
