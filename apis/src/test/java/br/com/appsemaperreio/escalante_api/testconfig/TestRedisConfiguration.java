package br.com.appsemaperreio.escalante_api.testconfig;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.service.BacklistService;
import br.com.appsemaperreio.escalante_api.shared.config.InMemoryBacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestRedisConfiguration {

    @Bean
    @Primary
    public BacklistService backlistService() {
        return new InMemoryBacklistService();
    }
}

