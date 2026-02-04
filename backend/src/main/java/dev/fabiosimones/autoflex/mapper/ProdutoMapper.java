package dev.fabiosimones.autoflex.mapper;

import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.ProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.ProdutoCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoResponse;
import dev.fabiosimones.autoflex.entities.MateriaPrimaEntity;
import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper implements MapperGenerico<
        ProdutoEntity,
        ProdutoResponse,
        ProdutoCriacaoRequest,
        ProdutoAtualizacaoRequest
        > {

    @Override
    public ProdutoResponse paraResponse(ProdutoEntity entidade) {
        return new ProdutoResponse(
                entidade.getId(),
                entidade.getNome(),
                entidade.getValor()
        );
    }

    @Override
    public ProdutoEntity paraEntidade(ProdutoCriacaoRequest request) {
        return ProdutoEntity.builder()
                .nome(request.nome())
                .valor(request.valor())
                .build();
    }

    @Override
    public void atualizarEntidade(ProdutoAtualizacaoRequest request, ProdutoEntity entidade) {
        entidade.setNome(request.nome());
        entidade.setValor(request.valor());
    }
}
