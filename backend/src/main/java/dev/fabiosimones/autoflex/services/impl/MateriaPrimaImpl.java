package dev.fabiosimones.autoflex.services.impl;

import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.MateriaPrimaCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.MateriaPrimaResponse;
import dev.fabiosimones.autoflex.entities.MateriaPrimaEntity;
import dev.fabiosimones.autoflex.mapper.MateriaPrimaMapper;
import dev.fabiosimones.autoflex.repositories.MateriaPrimaRepository;
import dev.fabiosimones.autoflex.services.MateriaPrimaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MateriaPrimaImpl implements MateriaPrimaService {

    private final MateriaPrimaRepository materiaPrimaRepository;
    private final MateriaPrimaMapper materiaPrimaMapper;

    @Override
    @Transactional
    public MateriaPrimaResponse criar(MateriaPrimaCriacaoRequest request) {
        MateriaPrimaEntity entity = materiaPrimaMapper.paraEntidade(request);
        MateriaPrimaEntity salvo = materiaPrimaRepository.save(entity);
        return materiaPrimaMapper.paraResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public MateriaPrimaResponse buscarPorId(Long id) {
        MateriaPrimaEntity entity = materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matéria-prima não encontrada: id=" + id));
        return materiaPrimaMapper.paraResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaPrimaResponse> listar() {
        return materiaPrimaRepository.findAll().stream()
                .map(materiaPrimaMapper::paraResponse)
                .toList();
    }

    @Override
    @Transactional
    public MateriaPrimaResponse atualizar(Long id, MateriaPrimaAtualizacaoRequest request) {
        MateriaPrimaEntity entity = materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matéria-prima não encontrada: id=" + id));

        materiaPrimaMapper.atualizarEntidade(request, entity);

        MateriaPrimaEntity atualizado = materiaPrimaRepository.save(entity);
        return materiaPrimaMapper.paraResponse(atualizado);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!materiaPrimaRepository.existsById(id)) {
            throw new EntityNotFoundException("Matéria-prima não encontrada: id=" + id);
        }
        materiaPrimaRepository.deleteById(id);
    }
}