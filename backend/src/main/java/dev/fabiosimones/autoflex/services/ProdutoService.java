package dev.fabiosimones.autoflex.services;

import dev.fabiosimones.autoflex.dtos.request.ProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.ProdutoCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoResponse;

import java.util.List;

public interface ProdutoService {

    ProdutoResponse criar(ProdutoCriacaoRequest request);
    ProdutoResponse buscarPorId(Long id);
    List<ProdutoResponse> listar();
    ProdutoResponse atualizar(Long id, ProdutoAtualizacaoRequest request);
    void excluir(Long id);
}
