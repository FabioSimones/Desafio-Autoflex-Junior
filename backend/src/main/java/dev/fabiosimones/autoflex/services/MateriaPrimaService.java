package dev.fabiosimones.autoflex.services;

import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.MateriaPrimaResponse;

import java.util.List;

public interface MateriaPrimaService {

    MateriaPrimaResponse criar(MateriaPrimaCriacaoRequest request);
    MateriaPrimaResponse buscarPorId(Long id);
    List<MateriaPrimaResponse> listar();
    MateriaPrimaResponse atualizar(Long id, MateriaPrimaAtualizacaoRequest request);
    void excluir(Long id);
}
