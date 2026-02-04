package dev.fabiosimones.autoflex.dtos.response;

import java.math.BigDecimal;

public record MateriaPrimaResponse(
        Long id,
        String nome,
        BigDecimal quantidadeEstoque
) {
}
