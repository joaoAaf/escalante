package br.com.appsemaperreio.escalante_api.config.seguranca;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Classe personalizada para lidar com tentativas de acesso não autorizado
public class AccessDeniedHandlerCustom implements AccessDeniedHandler {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(AccessDeniedHandlerCustom.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        LOG.debug("AccessDeniedException: {}", request.getRequestURI(), accessDeniedException.getMessage(), accessDeniedException);

        Map<String, String> body = new HashMap<>();
        body.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        body.put("Status", HttpStatus.FORBIDDEN.toString());
        body.put("Mensagem", "Acesso negado");

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), body);
    }

}
