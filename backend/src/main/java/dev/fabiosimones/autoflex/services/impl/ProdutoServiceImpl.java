package dev.fabiosimones.autoflex.services.impl;

import dev.fabiosimones.autoflex.dtos.request.ProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.request.ProdutoCriacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoResponse;
import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import dev.fabiosimones.autoflex.mapper.MapperGenerico;
import dev.fabiosimones.autoflex.mapper.MateriaPrimaMapper;
import dev.fabiosimones.autoflex.mapper.ProdutoMapper;
import dev.fabiosimones.autoflex.repositories.ProdutoRepository;
import dev.fabiosimones.autoflex.services.ProdutoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoMapper produtoMapper;

    @Override
    @Transactional
    public ProdutoResponse criar(ProdutoCriacaoRequest request) {
        ProdutoEntity entity = produtoMapper.paraEntidade(request);
        ProdutoEntity salvo = produtoRepository.save(entity);
        return produtoMapper.paraResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));
        return produtoMapper.paraResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::paraResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoAtualizacaoRequest request) {
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + id));

        produtoMapper.atualizarEntidade(request, entity);
        ProdutoEntity atualizado = produtoRepository.save(entity);

        return produtoMapper.paraResponse(atualizado);
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado: " + id);
        }
        produtoRepository.deleteById(id);
    }
}
