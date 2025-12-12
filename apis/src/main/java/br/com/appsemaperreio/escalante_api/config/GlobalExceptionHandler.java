package br.com.appsemaperreio.escalante_api.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import br.com.appsemaperreio.escalante_api.domain.escalante.exceptions.ErroLeituraPlanilhaModeloException;
import br.com.appsemaperreio.escalante_api.domain.escalante.exceptions.ErroProcessamentoPlanilhaException;
import br.com.appsemaperreio.escalante_api.domain.escalante.exceptions.PlanilhaModeloNaoEncontradaException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        LOG.debug("MethodArgumentNotValidException: {}", ex.getFieldError().getDefaultMessage());
        errors.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        errors.put("Status", HttpStatus.BAD_REQUEST.toString());
        ex.getBindingResult().getFieldErrors().stream().findFirst().ifPresent(error -> {
            errors.put("Mensagem", error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        LOG.debug("ConstraintViolationException: {}", ex.getMessage());
        errors.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        errors.put("Status", HttpStatus.BAD_REQUEST.toString());
        ex.getConstraintViolations().stream().findFirst().ifPresent(error -> {
            errors.put("Mensagem", error.getMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, String>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        LOG.debug("BindException: {}", ex.getFieldError().getDefaultMessage());
        errors.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        errors.put("Status", HttpStatus.BAD_REQUEST.toString());
        ex.getBindingResult().getFieldErrors().stream().findFirst().ifPresent(error -> {
            errors.put("Mensagem", error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("HttpMessageNotReadableException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.BAD_REQUEST.toString());
        error.put("Mensagem", "Corpo da requisição inválido ou mal formatado.");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("IllegalArgumentException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.BAD_REQUEST.toString());
        error.put("Mensagem", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("NoSuchElementException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.NOT_FOUND.toString());
        error.put("Mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PlanilhaModeloNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handlePlanilhaModeloNaoEncontrada(
            PlanilhaModeloNaoEncontradaException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.error("Planilha modelo não encontrada", ex);
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        error.put("Mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(ErroLeituraPlanilhaModeloException.class)
    public ResponseEntity<Map<String, String>> handleErroLeituraModelo(ErroLeituraPlanilhaModeloException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.error("Erro ao ler planilha modelo", ex);
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        error.put("Mensagem", ex.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(ErroProcessamentoPlanilhaException.class)
    public ResponseEntity<Map<String, String>> handleErroProcessamentoPlanilha(ErroProcessamentoPlanilhaException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.error("Erro ao processar planilha", ex);
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        error.put("Mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, String>> handleMultipartExceptions(MultipartException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("MultipartException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.BAD_REQUEST.toString());
        String mensagem;
        var msg = ex.getMessage();
        if (msg != null && msg.contains("Current request is not a multipart request")) {
            mensagem = "Requisição inválida: espera-se multipart/form-data com o arquivo anexado.";
        } else {
            mensagem = msg != null ? msg : "Erro no processamento do upload.";
        }
        error.put("Mensagem", mensagem);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestPart(MissingServletRequestPartException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("MissingServletRequestPartException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.BAD_REQUEST.toString());
        String partName = null;
        try {
            partName = ex.getRequestPartName();
        } catch (Exception e) {
            LOG.debug("Não foi possível obter request part name de MissingServletRequestPartException", e);
        }
        String mensagem = partName != null
                ? String.format("Requisição inválida: parte '%s' não foi enviada no form-data.", partName)
                : "Requisição inválida: parte esperada não foi enviada no form-data.";
        error.put("Mensagem", mensagem);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("MissingServletRequestParameterException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.BAD_REQUEST.toString());
        if (ex.getParameterName() == null) {
            error.put("Mensagem", "Parâmetro de requisição ausente.");
            return ResponseEntity.badRequest().body(error);
        }
        String mensagem = String.format("Parâmetro de requisição '%s' do tipo '%s' está ausente.",
                ex.getParameterName(), ex.getParameterType());
        error.put("Mensagem", mensagem);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        LOG.debug("AccessDeniedException: {}", ex.getMessage());
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.FORBIDDEN.toString());
        error.put("Mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> error = new HashMap<>();
        LOG.error("Erro inesperado", ex);
        error.put("Data/Hora", LocalDateTime.now().format(FORMATO_DATA));
        error.put("Status", HttpStatus.SERVICE_UNAVAILABLE.toString());
        error.put("Mensagem", "Serviço indisponível no momento. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

}
