package dev.fabiosimones.autoflex.dtos.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemEstruturaProdutoRequest(
        @NotNull(message = "O id da matéria-prima é obrigatório.")
        Long materiaPrimaId,

        @NotNull(message = "A quantidade necessária é obrigatória.")
        @Positive(message = "A quantidade necessária deve ser maior que zero.")
        @Digits(integer = 12, fraction = 3, message = "A quantidade necessária deve ter no máximo 12 dígitos inteiros e 3 decimais.")
        BigDecimal quantidadeNecessaria
) {
}
