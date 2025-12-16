package br.com.appsemaperreio.escalante_api.seguranca.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import br.com.appsemaperreio.escalante_api.seguranca.config.custom_exceptions.AccessDeniedHandlerCustom;
import br.com.appsemaperreio.escalante_api.seguranca.config.custom_exceptions.AuthenticationEntryPointCustom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Environment env;

    public SecurityConfig(Environment env) {
        this.env = env;
    }

    // Filtro de segurança para a rota de login, com utilização exclusiva do HTTP Basic
    @Bean
    @Order(1)
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/login", "/api/usuarios/username")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AuthenticationEntryPointCustom())
                        .accessDeniedHandler(new AccessDeniedHandlerCustom()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // Filtro de segurança para as demais rotas da API, com utilização exclusiva de token JWT
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        // Configuração do conversor de autoridades baseado no claim "roles" do JWT
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("perfis");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        var devProfileActive = List.of(env.getActiveProfiles()).contains("dev");

        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> {
                    // Para possibilitar o acesso ao console H2
                    if (devProfileActive)
                        headers.frameOptions(frameOptions -> frameOptions.sameOrigin());
                })
                .authorizeHttpRequests(auth -> {
                    if (devProfileActive)
                        auth.requestMatchers("/h2/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                // Configuração do recurso OAuth2 como um Resource Server que utiliza JWT
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                // Configuração de tratamento de exceções
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AuthenticationEntryPointCustom())
                        .accessDeniedHandler(new AccessDeniedHandlerCustom()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    // Beans para codificação e decodificação de JWTs utilizando uma chave secreta HMAC SHA-256
    @Bean
    JwtDecoder jwtDecoder() {
        var jwtSecret = env.getProperty("env.jwt.secret");
        var secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        var jwtSecret = env.getProperty("env.jwt.secret");
        var secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}