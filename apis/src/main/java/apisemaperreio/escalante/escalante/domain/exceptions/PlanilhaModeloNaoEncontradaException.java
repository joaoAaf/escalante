package apisemaperreio.escalante.escalante.domain.exceptions;

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
