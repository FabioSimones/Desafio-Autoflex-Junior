package dev.fabiosimones.autoflex.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EstruturaProdutoAtualizacaoRequest(
        @NotNull(message = "A lista de matérias-primas do produto é obrigatória.")
        @Size(min = 1, message = "Informe ao menos 1 matéria-prima na estrutura do produto.")
        @Valid
        List<ItemEstruturaProdutoRequest> itens
) {
}
