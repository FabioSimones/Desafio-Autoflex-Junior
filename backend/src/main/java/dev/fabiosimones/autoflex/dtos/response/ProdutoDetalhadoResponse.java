package dev.fabiosimones.autoflex.dtos.response;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoDetalhadoResponse(
        Long id,
        String nome,
        BigDecimal valor,
        List<ItemEstruturaProdutoResponse> materiasPrimas
) {
}
