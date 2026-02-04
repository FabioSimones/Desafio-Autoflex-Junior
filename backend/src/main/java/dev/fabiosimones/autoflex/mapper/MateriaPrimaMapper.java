package dev.fabiosimones.autoflex.mapper;

import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.MateriaPrimaResponse;
import dev.fabiosimones.autoflex.entities.MateriaPrimaEntity;
import org.springframework.stereotype.Component;

@Component
public class MateriaPrimaMapper implements MapperGenerico<MateriaPrimaEntity, MateriaPrimaResponse, MateriaPrimaCriacaoRequest, MateriaPrimaAtualizacaoRequest> {

    @Override
    public MateriaPrimaResponse paraResponse(MateriaPrimaEntity entidade) {
        return new MateriaPrimaResponse(
                entidade.getId(),
                entidade.getNome(),
                entidade.getQuantidadeEstoque()
        );
    }

    @Override
    public MateriaPrimaEntity paraEntidade(MateriaPrimaCriacaoRequest request) {
        return MateriaPrimaEntity.builder()
                .nome(request.nome())
                .quantidadeEstoque(request.quantidadeEstoque())
                .build();
    }

    @Override
    public void atualizarEntidade(MateriaPrimaAtualizacaoRequest request, MateriaPrimaEntity entidade) {
        entidade.setNome(request.nome());
        entidade.setQuantidadeEstoque(request.quantidadeEstoque());
    }

}