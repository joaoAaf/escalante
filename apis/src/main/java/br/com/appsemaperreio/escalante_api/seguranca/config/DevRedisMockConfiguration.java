package br.com.appsemaperreio.escalante_api.seguranca.config;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.service.BacklistService;
import br.com.appsemaperreio.escalante_api.shared.config.InMemoryBacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevRedisMockConfiguration {

    @Bean
    @Primary
    public BacklistService backlistService() {
        return new InMemoryBacklistService();
    }
}
