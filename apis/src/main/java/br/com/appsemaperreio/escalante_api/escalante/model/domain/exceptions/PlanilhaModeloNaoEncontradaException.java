package br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions;

public class PlanilhaModeloNaoEncontradaException extends RuntimeException {

    public PlanilhaModeloNaoEncontradaException(String message) {
        super(message);
    }

    public PlanilhaModeloNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlanilhaModeloNaoEncontradaException(Throwable cause) {
        super(cause);
    }

}
