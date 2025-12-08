package br.com.appsemaperreio.escalante_api.configs.seguranca;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Classe personalizada para lidar com tentativas de acesso não autenticado
public class AuthenticationEntryPointCustom implements AuthenticationEntryPoint {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();
        body.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        body.put("Status", HttpStatus.UNAUTHORIZED.toString());
        String msg = authException != null && authException.getMessage() != null ? authException.getMessage()
                : "Acesso não autorizado";
        body.put("Mensagem", msg);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getOutputStream(), body);
    }

}
