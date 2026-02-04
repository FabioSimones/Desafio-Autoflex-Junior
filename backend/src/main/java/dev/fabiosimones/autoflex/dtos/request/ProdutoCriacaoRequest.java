package dev.fabiosimones.autoflex.dtos.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoCriacaoRequest(

        @NotBlank(message = "O nome do produto é obrigatório.")
        @Size(max = 120, message = "O nome do produto deve ter no máximo 120 caracteres.")
        String nome,

        @NotNull(message = "O valor do produto é obrigatório.")
        @PositiveOrZero(message = "O valor do produto deve ser maior ou igual a zero.")
        @Digits(integer = 13, fraction = 2, message = "O valor do produto deve ter no máximo 13 dígitos inteiros e 2 decimais.")
        BigDecimal valor
) {
}
