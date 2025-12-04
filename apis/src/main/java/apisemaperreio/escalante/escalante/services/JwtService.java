package apisemaperreio.escalante.escalante.services;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final String emissor;

    public JwtService(JwtEncoder encoder, @Value("${spring.application.name}") String emissor) {
        this.encoder = encoder;
        this.emissor = emissor;
    }

    public String gerarToken(Authentication authentication) {
        Instant instanteAtual = Instant.now();
        long tempoExpiracao = 3600L;

        var roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        
        var claims = JwtClaimsSet.builder()
                .issuer(this.emissor)
                .issuedAt(instanteAtual)
                .expiresAt(instanteAtual.plusSeconds(tempoExpiracao))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        var jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
