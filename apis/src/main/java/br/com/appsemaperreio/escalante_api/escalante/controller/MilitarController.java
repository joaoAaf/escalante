package br.com.appsemaperreio.escalante_api.escalante.controller;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.model.application.IMilitarService;
import jakarta.validation.constraints.NotNull;

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
    public ResponseEntity<?> importarMilitaresXLSX(
            @RequestParam @NotNull(message = "O arquivo de militares não pode ser nulo.") MultipartFile militares) {
        return ResponseEntity.ok(service.importarMilitaresXLSX(militares));
    }

    @PostMapping
    public ResponseEntity<List<MilitarDto>> cadastrarMilitares(
            @RequestBody
            @NotEmpty(message = "A lista de militares não pode estar vazia.")
            List<@Valid MilitarDto> militaresDto) {
        List<MilitarDto> militaresCadastrados = service.cadastrarMilitares(militaresDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(militaresCadastrados);
    }

}
