package br.com.appsemaperreio.escalante_api.seguranca.config;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.service.BacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class BlacklistFilter extends OncePerRequestFilter {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(BlacklistFilter.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final BacklistService blacklistService;

    public BlacklistFilter(BacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {

            var jti = jwtAuth.getToken().getId();

            if (jti != null && blacklistService.revogado(jti)) {
                SecurityContextHolder.clearContext();

                LOG.debug("Token com jti {} está na blacklist. Acesso negado para {}", jti, request.getRequestURI());

                Map<String, String> body = new HashMap<>();
                body.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
                body.put("Status", HttpStatus.UNAUTHORIZED.toString());
                body.put("Mensagem", "Token inválido ou expirado");

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getOutputStream(), body);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
