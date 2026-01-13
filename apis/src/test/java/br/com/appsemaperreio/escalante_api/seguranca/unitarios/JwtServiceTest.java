package br.com.appsemaperreio.escalante_api.seguranca.unitarios;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.service.JwtService;

public class JwtServiceTest {

    private static final String TEST_SECRET = "test-secret-key-for-jwt-unit-tests-which-is-long-enough";

    @Test
    void deveConterClaimsEPerfisAoGerarToken() {
        var secretKey = new SecretKeySpec(TEST_SECRET.getBytes(), "HmacSHA256");
        var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        var jwtService = new JwtService(encoder, "escalante-api", 3600L);

        Authentication auth = new UsernamePasswordAuthenticationToken("alice", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtService.gerarToken(auth);
        assertNotNull(token);

        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        Jwt decoded = decoder.decode(token);

        assertEquals("escalante-api", decoded.getClaimAsString("iss"));
        assertEquals("alice", decoded.getSubject());

        @SuppressWarnings("unchecked")
        var perfis = (List<String>) decoded.getClaims().get("scope");
        assertNotNull(perfis);
        assertTrue(perfis.contains("ADMIN"));
        assertTrue(perfis.contains("USER"));

        Instant issuedAt = decoded.getIssuedAt();
        Instant expiresAt = decoded.getExpiresAt();
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);
        assertTrue(expiresAt.isAfter(issuedAt));
    }

    @Test
    void deveMarcarExpiracaoCorretamenteAoGerarToken() throws InterruptedException {
        var secretKey = new SecretKeySpec(TEST_SECRET.getBytes(), "HmacSHA256");
        var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
        var jwtService = new JwtService(encoder, "escalante-api", 1L);

        Authentication auth = new UsernamePasswordAuthenticationToken("bob", null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtService.gerarToken(auth);

        Thread.sleep(1200);

        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
        Jwt decoded = decoder.decode(token);
        assertTrue(decoded.getExpiresAt().isBefore(Instant.now()));
    }

}
