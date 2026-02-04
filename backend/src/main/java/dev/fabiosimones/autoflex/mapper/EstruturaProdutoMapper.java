package dev.fabiosimones.autoflex.mapper;

import dev.fabiosimones.autoflex.dtos.response.ItemEstruturaProdutoResponse;
import dev.fabiosimones.autoflex.dtos.response.ProdutoDetalhadoResponse;
import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import org.springframework.stereotype.Component;

@Component
public class EstruturaProdutoMapper {

    public ProdutoDetalhadoResponse paraDetalhadoResponse(ProdutoEntity produto) {
        var itens = produto.getMateriasPrimas().stream()
                .map(pm -> new ItemEstruturaProdutoResponse(
                        pm.getMateriaPrima().getId(),
                        pm.getMateriaPrima().getNome(),
                        pm.getQuantidadeNecessaria()
                ))
                .toList();

        return new ProdutoDetalhadoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getValor(),
                itens
        );
    }
}
