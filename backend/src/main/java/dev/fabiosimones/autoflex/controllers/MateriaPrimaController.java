package dev.fabiosimones.autoflex.controllers;

import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.MateriaPrimaResponse;
import dev.fabiosimones.autoflex.services.MateriaPrimaService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/materias-primas")
@RequiredArgsConstructor
public class MateriaPrimaController {

    private final MateriaPrimaService materiaPrimaService;

    @Operation(summary = "Listar matérias-primas", description = "Retorna todas as matérias-primas cadastradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<MateriaPrimaResponse>> listar() {
        return ResponseEntity.ok(materiaPrimaService.listar());
    }

    @Operation(summary = "Buscar matéria-prima por ID", description = "Retorna uma matéria-prima pelo id.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Matéria-prima encontrada",
                    content = @Content(schema = @Schema(implementation = MateriaPrimaResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Matéria-prima não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MateriaPrimaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materiaPrimaService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar matéria-prima", description = "Cria uma nova matéria-prima.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Matéria-prima criada com sucesso",
                    content = @Content(schema = @Schema(implementation = MateriaPrimaResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<MateriaPrimaResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação da matéria-prima",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MateriaPrimaCriacaoRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo: Aço",
                                            value = """
                            {
                              "nome": "Aço",
                              "quantidadeEstoque": 100.000
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "Exemplo: Plástico",
                                            value = """
                            {
                              "nome": "Plástico",
                              "quantidadeEstoque": 250.500
                            }
                            """
                                    )
                            }
                    )
            )
            @Valid @RequestBody MateriaPrimaCriacaoRequest request
    ) {
        MateriaPrimaResponse criado = materiaPrimaService.criar(request);
        return ResponseEntity
                .created(URI.create("/api/materias-primas/" + criado.id()))
                .body(criado);
    }

    @Operation(summary = "Atualizar matéria-prima", description = "Atualiza nome e quantidade em estoque.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Matéria-prima atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = MateriaPrimaResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Matéria-prima não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrimaResponse> atualizar(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização da matéria-prima",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MateriaPrimaAtualizacaoRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de atualização",
                                    value = """
                        {
                          "nome": "Aço (Atualizado)",
                          "quantidadeEstoque": 80.500
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody MateriaPrimaAtualizacaoRequest request
    ) {
        return ResponseEntity.ok(materiaPrimaService.atualizar(id, request));
    }

    @Operation(summary = "Excluir matéria-prima", description = "Remove uma matéria-prima pelo id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Matéria-prima removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Matéria-prima não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        materiaPrimaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
