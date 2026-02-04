package dev.fabiosimones.autoflex.controllers;

import dev.fabiosimones.autoflex.dtos.request.EstruturaProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoDetalhadoResponse;
import dev.fabiosimones.autoflex.services.EstruturaProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/produtos")
public class ProdutoMateriaPrimaController {

    private final EstruturaProdutoService estruturaProdutoService;

    @Operation(
            summary = "Buscar matérias-primas de um produto",
            description = "Retorna o produto com a lista de matérias-primas e as quantidades necessárias para produzir 1 unidade."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estrutura retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDetalhadoResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{produtoId}/materias-primas")
    public ResponseEntity<ProdutoDetalhadoResponse> buscarEstrutura(@PathVariable Long produtoId) {
        return ResponseEntity.ok(estruturaProdutoService.buscarProdutoComEstrutura(produtoId));
    }

    @Operation(
            summary = "Atualizar matérias-primas de um produto",
            description = "Substitui toda a estrutura do produto (PUT). Ideal para edição no cadastro do produto."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estrutura atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDetalhadoResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto ou matéria-prima não encontrada")
    })
    @PutMapping("/{produtoId}/materias-primas")
    public ResponseEntity<ProdutoDetalhadoResponse> atualizarEstrutura(
            @PathVariable Long produtoId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lista de matérias-primas e quantidades necessárias",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EstruturaProdutoAtualizacaoRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de estrutura (2 itens)",
                                            value = """
                            {
                              "itens": [
                                { "materiaPrimaId": 1, "quantidadeNecessaria": 2.000 },
                                { "materiaPrimaId": 2, "quantidadeNecessaria": 0.250 }
                              ]
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "Exemplo de estrutura (1 item)",
                                            value = """
                            {
                              "itens": [
                                { "materiaPrimaId": 3, "quantidadeNecessaria": 1.500 }
                              ]
                            }
                            """
                                    )
                            }
                    )
            )
            @Valid @RequestBody EstruturaProdutoAtualizacaoRequest request
    ) {
        return ResponseEntity.ok(estruturaProdutoService.atualizarEstrutura(produtoId, request));
    }
}
