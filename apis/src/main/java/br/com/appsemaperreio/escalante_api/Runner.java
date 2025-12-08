package br.com.appsemaperreio.escalante_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Admin;
import br.com.appsemaperreio.escalante_api.repository.seguranca.AdminRepository;

@Configuration
public class Runner implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Runner(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        var admin = new Admin("joao", passwordEncoder.encode("123456"));

        adminRepository.save(admin);
    }

}
