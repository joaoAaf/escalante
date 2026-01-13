package br.com.appsemaperreio.escalante_api.escalante.controller;

import br.com.appsemaperreio.escalante_api.escalante.model.application.IMilitarService;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Validated
@RequestMapping("/api/militar")
@RestController
public class MilitarController {

    private final IMilitarService service;

    public MilitarController(IMilitarService service) {
        this.service = service;
    }

    @GetMapping("/modelo/xlsx")
    public ResponseEntity<byte[]> obterPlanilhaModeloMilitares() {
        var planilhaModelo = service.obterPlanilhaModeloMilitares();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=modelo_importacao_militares.xlsx")
                .contentType(MediaType
                        .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(planilhaModelo);
    }

    @PostMapping("/importar/xlsx")
    public ResponseEntity<List<MilitarDto>> importarMilitaresXLSX(
            @RequestParam @NotNull(message = "O arquivo de militares não pode ser nulo.") MultipartFile militares) {
        return ResponseEntity.ok(service.importarMilitaresXLSX(militares));
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarMilitares(
            @RequestBody
            @NotEmpty(message = "A lista de militares não pode estar vazia.")
            List<@Valid MilitarDto> militaresDto) {
        service.cadastrarMilitares(militaresDto);
        return ResponseEntity.created(URI.create("/api/militar")).build();
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<MilitarDto> obterMilitarPorMatricula(
            @PathVariable @NotBlank(message = "A matrícula do militar não pode ser nula ou vazia.") String matricula) {
        MilitarDto militarDto = service.obterMilitarPorMatricula(matricula);
        return ResponseEntity.ok(militarDto);
    }

    @GetMapping
    public ResponseEntity<List<MilitarDto>> listarMilitares() {
        List<MilitarDto> militares = service.listarMilitares();
        if (militares.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(militares);
    }

    @PutMapping
    public ResponseEntity<Void> atualizarMilitar(
            @RequestBody @NotNull(message = "O militar não pode ser nulo.") @Valid MilitarDto militarDto) {
        service.atualizarMilitar(militarDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletarMilitar(
            @PathVariable @NotBlank(message = "A matrícula do militar não pode ser nula ou vazia.") String matricula) {
        service.deletarMilitar(matricula);
        return ResponseEntity.noContent().build();
    }

}
