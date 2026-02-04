package dev.fabiosimones.autoflex.mapper;

public interface MapperGenerico <ENTIDADE, RESPONSE, REQUEST_CRIACAO>{

    RESPONSE paraResponse(ENTIDADE entidade);

    ENTIDADE paraEntidade(REQUEST_CRIACAO request);

    void atualizarEntidade(REQUEST_CRIACAO request, ENTIDADE entidade);
}
