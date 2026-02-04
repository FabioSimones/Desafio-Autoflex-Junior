package dev.fabiosimones.autoflex.controllers;

import dev.fabiosimones.autoflex.dtos.request.ProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.ProdutoCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoResponse;
import dev.fabiosimones.autoflex.services.ProdutoService;
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
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto pelo seu id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar produto", description = "Cria um novo produto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação do produto",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ProdutoCriacaoRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de cadastro",
                                    value = """
                        {
                          "nome": "Produto A",
                          "valor": 199.90
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody ProdutoCriacaoRequest request
    ) {
        ProdutoResponse criado = produtoService.criar(request);
        return ResponseEntity
                .created(URI.create("/api/produtos/" + criado.id()))
                .body(criado);
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza nome e valor de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para atualização do produto",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ProdutoAtualizacaoRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de atualização",
                                    value = """
                        {
                          "nome": "Produto A (Atualizado)",
                          "valor": 249.90
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody ProdutoAtualizacaoRequest request
    ) {
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    @Operation(summary = "Excluir produto", description = "Remove um produto pelo id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
