package br.com.appsemaperreio.escalante_api.seguranca.model.application.service;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.ILogoutService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements ILogoutService {

    private final BacklistService blacklistService;

    public LogoutService(BacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Override
    public void logout(Jwt jwt) {
        if (jwt.getExpiresAt() == null || jwt.getIssuedAt() == null)
            throw new IllegalArgumentException("Token inválido: datas de emissão ou expiração ausentes");
        var expiracao = jwt.getExpiresAt().getEpochSecond() - jwt.getIssuedAt().getEpochSecond();
        if (expiracao <= 0)
            throw new IllegalArgumentException("Token já expirado ou com datas inválidas");
        blacklistService.addBlacklist(jwt.getId(), expiracao);
    }

}
