package dev.fabiosimones.autoflex.services;

import dev.fabiosimones.autoflex.dtos.request.EstruturaProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoDetalhadoResponse;

public interface EstruturaProdutoService {

    ProdutoDetalhadoResponse buscarProdutoComEstrutura(Long produtoId);
    ProdutoDetalhadoResponse atualizarEstrutura(Long produtoId, EstruturaProdutoAtualizacaoRequest request);

}
