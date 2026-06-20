package br.com.appsemaperreio.escalante_api.shared.config;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.service.BacklistService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBacklistService extends BacklistService {

    private final Map<String, Long> store = new ConcurrentHashMap<>();

    public InMemoryBacklistService() {
        super(null);
    }

    @Override
    public void addBlacklist(String jti, long expiracao) {
        if (jti == null) return;
        long expireAt = System.currentTimeMillis() + (expiracao * 1000L);
        store.put(jti, expireAt);
    }

    @Override
    public boolean revogado(String jti) {
        if (jti == null) return false;
        Long exp = store.get(jti);
        if (exp == null) return false;
        if (System.currentTimeMillis() > exp) {
            store.remove(jti);
            return false;
        }
        return true;
    }
}
