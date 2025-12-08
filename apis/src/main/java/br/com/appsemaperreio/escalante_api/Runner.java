package br.com.appsemaperreio.escalante_api;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;
import br.com.appsemaperreio.escalante_api.domain.seguranca.Usuario;
import br.com.appsemaperreio.escalante_api.repository.seguranca.UsuarioRepository;

@Configuration
public class Runner implements CommandLineRunner {

    private final UsuarioRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Runner(UsuarioRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        var admin = new Usuario("joao", passwordEncoder.encode("123456"), Set.of(Perfil.ADMIN));

        adminRepository.save(admin);
    }

}
