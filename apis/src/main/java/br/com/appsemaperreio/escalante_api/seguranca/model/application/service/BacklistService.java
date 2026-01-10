package br.com.appsemaperreio.escalante_api.seguranca.model.application.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BacklistService {

    private final StringRedisTemplate redisTemplate;

    public BacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addBlacklist(String jti, long expiracao) {
        redisTemplate.opsForValue().set(jti, "revoked", Duration.ofSeconds(expiracao));
    }

    public boolean revogado(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(jti));
    }

}
