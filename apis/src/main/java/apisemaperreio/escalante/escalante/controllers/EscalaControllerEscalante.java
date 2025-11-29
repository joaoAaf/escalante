package apisemaperreio.escalante.escalante.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import apisemaperreio.escalante.escalante.dtos.DadosEscalaRequest;
import apisemaperreio.escalante.escalante.dtos.ServicoOperacionalDto;
import apisemaperreio.escalante.escalante.usecases.EscalaUseCasesEscalante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@RequestMapping("/api/escala")
@RestController
public class EscalaControllerEscalante {

    @Autowired
    private EscalaUseCasesEscalante escalaUseCases;

    @GetMapping("/modelo/xlsx")
    public ResponseEntity<byte[]> obterPlanilhaModeloEscala() {
        try {
            var planilhaModelo = escalaUseCases.obterPlanilhaModeloEscala();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=modelo_planilha_escala.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(planilhaModelo);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/importar/xlsx")
    public ResponseEntity<?> importarEscalaXLSX(
            @RequestParam @NotNull(message = "O arquivo de escala não pode ser nulo.") MultipartFile escala) {
        try {
            return ResponseEntity.ok(escalaUseCases.importarEscalaXLSX(escala));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criarEscalaAutomatica(
            @RequestBody @NotNull(message = "Os dados da escala não podem ser nulos.") @Valid DadosEscalaRequest request) {
        try {
            return ResponseEntity.ok(escalaUseCases.criarEscalaAutomatica(request));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/exportar/xlsx")
    public ResponseEntity<byte[]> exportarEscalaXLSX(
            @RequestBody
            @NotNull(message = "A lista de serviços operacionais não pode ser nula.")
            @NotEmpty(message = "A lista de serviços operacionais não pode estar vazia.")
            @Size(max = 210, message = "A lista de serviços operacionais não pode conter mais de 210 serviços.")
            List<@Valid ServicoOperacionalDto> servicos) {
        try {
            var escalaXLSX = escalaUseCases.exportarEscalaXLSX(servicos);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=escala.xlsx")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(escalaXLSX);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

}
