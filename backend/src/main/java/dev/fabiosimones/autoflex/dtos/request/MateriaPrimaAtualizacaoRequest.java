package dev.fabiosimones.autoflex.dtos.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MateriaPrimaAtualizacaoRequest(
        @NotBlank(message = "O nome da matéria-prima é obrigatório.")
        @Size(max = 120, message = "O nome da matéria-prima deve ter no máximo 120 caracteres.")
        String nome,

        @NotNull(message = "A quantidade em estoque é obrigatória.")
        @PositiveOrZero(message = "A quantidade em estoque deve ser maior ou igual a zero.")
        @Digits(integer = 12, fraction = 3, message = "A quantidade em estoque deve ter no máximo 12 dígitos inteiros e 3 decimais.")
        BigDecimal quantidadeEstoque
) {
}
