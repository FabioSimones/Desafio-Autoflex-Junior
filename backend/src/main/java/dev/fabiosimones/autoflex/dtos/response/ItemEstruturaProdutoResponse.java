package dev.fabiosimones.autoflex.dtos.response;

import java.math.BigDecimal;

public record ItemEstruturaProdutoResponse(
        Long materiaPrimaId,
        String nomeMateriaPrima,
        BigDecimal quantidadeNecessaria
) {
}
