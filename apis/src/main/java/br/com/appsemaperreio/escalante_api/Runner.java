package br.com.appsemaperreio.escalante_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.IUsuarioService;

@Configuration
public class Runner implements CommandLineRunner {

    private final IUsuarioService usuarioUseCases;

    public Runner(IUsuarioService usuarioUseCases) {
        this.usuarioUseCases = usuarioUseCases;
    }

    @Override
    public void run(String... args) throws Exception {

        usuarioUseCases.cadastrarUsuarioInicial();

    }

}
