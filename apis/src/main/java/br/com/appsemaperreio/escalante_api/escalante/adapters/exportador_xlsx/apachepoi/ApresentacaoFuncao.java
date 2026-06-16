package br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx.apachepoi;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;

public record ApresentacaoFuncao(
        String label,
        int ordemExibicao,
        Funcao funcao,
        Integer linha) {

}
