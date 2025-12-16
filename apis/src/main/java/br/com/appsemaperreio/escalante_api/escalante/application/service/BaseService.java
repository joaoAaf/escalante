package br.com.appsemaperreio.escalante_api.escalante.application.service;

import org.springframework.web.multipart.MultipartFile;

public abstract class BaseService {

    protected void validarPlanilha(MultipartFile planilha) {
        if (planilha == null || planilha.isEmpty())
            throw new IllegalArgumentException("A planilha enviada não pode estar vazia ou nula.");

        var contentType = planilha.getContentType();

        if (contentType == null
                || !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            throw new IllegalArgumentException("O arquivo enviado não é uma planilha XLSX válida.");
    }
}
