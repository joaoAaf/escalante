package br.com.appsemaperreio.escalante_api.controllers.escalante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.usecases.escalante.MilitarUseCasesEscalante;
import jakarta.validation.constraints.NotNull;

@Validated
@RequestMapping("/api/militar")
@RestController
public class MilitarControllerEscalante {

    @Autowired
    private MilitarUseCasesEscalante militarUseCases;

    @GetMapping("/modelo/xlsx")
    public ResponseEntity<byte[]> obterPlanilhaModeloMilitares() {
    var planilhaModelo = militarUseCases.obterPlanilhaModeloMilitares();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=modelo_importacao_militares.xlsx")
        .contentType(MediaType
            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(planilhaModelo);
    }

    @PostMapping("/importar/xlsx")
    public ResponseEntity<?> importarMilitaresXLSX(
            @RequestParam @NotNull(message = "O arquivo de militares não pode ser nulo.") MultipartFile militares) {
        return ResponseEntity.ok(militarUseCases.importarMilitaresXLSX(militares));
    }

}
