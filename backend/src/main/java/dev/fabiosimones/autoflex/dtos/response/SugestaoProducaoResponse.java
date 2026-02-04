package dev.fabiosimones.autoflex.dtos.response;

import java.math.BigDecimal;
import java.util.List;

public record SugestaoProducaoResponse(
        List<SugestaoProducaoItemResponse> itens,
        BigDecimal valorTotalProducao
) {
}
