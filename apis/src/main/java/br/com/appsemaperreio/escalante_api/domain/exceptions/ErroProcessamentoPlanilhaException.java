package br.com.appsemaperreio.escalante_api.domain.exceptions;

public class ErroProcessamentoPlanilhaException extends RuntimeException {

    public ErroProcessamentoPlanilhaException(String message) {
        super(message);
    }

    public ErroProcessamentoPlanilhaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroProcessamentoPlanilhaException(Throwable cause) {
        super(cause);
    }

}
