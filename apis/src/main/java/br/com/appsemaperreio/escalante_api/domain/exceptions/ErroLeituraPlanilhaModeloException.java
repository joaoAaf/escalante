package br.com.appsemaperreio.escalante_api.domain.exceptions;

public class ErroLeituraPlanilhaModeloException extends RuntimeException {

    public ErroLeituraPlanilhaModeloException(String message) {
        super(message);
    }

    public ErroLeituraPlanilhaModeloException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroLeituraPlanilhaModeloException(Throwable cause) {
        super(cause);
    }

}
