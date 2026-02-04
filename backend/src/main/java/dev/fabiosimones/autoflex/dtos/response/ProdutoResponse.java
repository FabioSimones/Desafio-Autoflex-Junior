package dev.fabiosimones.autoflex.dtos.response;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        BigDecimal valor
) {
}
