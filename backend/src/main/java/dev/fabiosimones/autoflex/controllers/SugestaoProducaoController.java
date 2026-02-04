package dev.fabiosimones.autoflex.controllers;

import dev.fabiosimones.autoflex.dtos.response.SugestaoProducaoResponse;
import dev.fabiosimones.autoflex.services.SugestaoProducaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SugestaoProducaoController {
    private final SugestaoProducaoService sugestaoProducaoService;

    @Operation(
            summary = "Sugestão de produção",
            description = "Calcula quais produtos e quantas unidades podem ser produzidas com base no estoque atual, priorizando produtos de maior valor."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sugestão calculada com sucesso")
    })
    @GetMapping("/sugestao-producao")
    public ResponseEntity<SugestaoProducaoResponse> calcular() {
        return ResponseEntity.ok(sugestaoProducaoService.calcularSugestao());
    }
}

