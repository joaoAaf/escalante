package br.com.appsemaperreio.escalante_api.domain.escalante.factories;

import java.util.function.Function;

import br.com.appsemaperreio.escalante_api.domain.escalante.DadosEscala;
import br.com.appsemaperreio.escalante_api.domain.escalante.Escala;

public final class EscalaFactory {

    private static Function<DadosEscala, Escala> escalaFactory = Escala::new;

    private EscalaFactory() {} 

    public static Escala criarEscala(DadosEscala dados) {
        return escalaFactory.apply(dados);
    }

}
