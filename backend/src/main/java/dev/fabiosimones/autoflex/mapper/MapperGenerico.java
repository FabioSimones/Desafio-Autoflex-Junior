package dev.fabiosimones.autoflex.mapper;

public interface MapperGenerico <ENTIDADE, RESPONSE, REQ_CRIACAO, REQ_ATUALIZACAO> {

    RESPONSE paraResponse(ENTIDADE entidade);

    ENTIDADE paraEntidade(REQ_CRIACAO request);

    void atualizarEntidade(REQ_ATUALIZACAO request, ENTIDADE entidade);
}
