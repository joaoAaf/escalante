package apisemaperreio.escalante.escalante.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class loginService {

    private final JwtService jwtService;

    public loginService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String login(Authentication authentication) {
        return jwtService.gerarToken(authentication);
    }
}
