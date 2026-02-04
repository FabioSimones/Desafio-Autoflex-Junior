package dev.fabiosimones.autoflex.dtos.response;

import java.math.BigDecimal;

public record SugestaoProducaoItemResponse(
        Long produtoId,
        String nomeProduto,
        BigDecimal valorUnitario,
        Long quantidadeProduzivel,
        BigDecimal valorTotalProduto
) {
}
