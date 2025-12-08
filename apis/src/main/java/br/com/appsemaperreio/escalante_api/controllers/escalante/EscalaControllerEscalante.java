package br.com.appsemaperreio.escalante_api.controllers.escalante;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dtos.escalante.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.dtos.escalante.ServicoOperacionalDto;
import br.com.appsemaperreio.escalante_api.usecases.escalante.EscalaUseCasesEscalante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Validated
@RequestMapping("/api/escala")
@RestController
public class EscalaControllerEscalante {

    @Autowired
    private EscalaUseCasesEscalante escalaUseCases;

    @GetMapping("/modelo/xlsx")
    public ResponseEntity<byte[]> obterPlanilhaModeloEscala() {
    var planilhaModelo = escalaUseCases.obterPlanilhaModeloEscala();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=modelo_planilha_escala.xlsx")
        .contentType(MediaType
            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(planilhaModelo);
    }

    @PostMapping("/importar/xlsx")
    public ResponseEntity<?> importarEscalaXLSX(
            @RequestParam @NotNull(message = "O arquivo de escala não pode ser nulo.") MultipartFile escala) {
        return ResponseEntity.ok(escalaUseCases.importarEscalaXLSX(escala));
    }

    @PostMapping
    public ResponseEntity<?> criarEscalaAutomatica(
            @RequestBody @NotNull(message = "Os dados da escala não podem ser nulos.") @Valid DadosEscalaRequest request) {
        return ResponseEntity.ok(escalaUseCases.criarEscalaAutomatica(request));
    }

    @PostMapping("/exportar/xlsx")
    public ResponseEntity<byte[]> exportarEscalaXLSX(
            @RequestBody
            @NotNull(message = "A lista de serviços operacionais não pode ser nula.")
            @NotEmpty(message = "A lista de serviços operacionais não pode estar vazia.")
            @Size(max = 210, message = "A lista de serviços operacionais não pode conter mais de 210 serviços.")
        List<@Valid ServicoOperacionalDto> servicos) throws Exception {
    var escalaXLSX = escalaUseCases.exportarEscalaXLSX(servicos);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=escala.xlsx")
        .contentType(MediaType
            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(escalaXLSX);
    }

}
