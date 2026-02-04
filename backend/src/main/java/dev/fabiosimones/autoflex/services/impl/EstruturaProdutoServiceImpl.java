package dev.fabiosimones.autoflex.services.impl;

import dev.fabiosimones.autoflex.dtos.request.EstruturaProdutoAtualizacaoRequest;
import dev.fabiosimones.autoflex.dtos.response.ProdutoDetalhadoResponse;
import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import dev.fabiosimones.autoflex.entities.ProdutoMateriaPrimaEntity;
import dev.fabiosimones.autoflex.mapper.EstruturaProdutoMapper;
import dev.fabiosimones.autoflex.repositories.MateriaPrimaRepository;
import dev.fabiosimones.autoflex.repositories.ProdutoRepository;
import dev.fabiosimones.autoflex.services.EstruturaProdutoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class EstruturaProdutoServiceImpl implements EstruturaProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;
    private final EstruturaProdutoMapper estruturaProdutoMapper;

    @Override
    @Transactional(readOnly = true)
    public ProdutoDetalhadoResponse buscarProdutoComEstrutura(Long produtoId) {
        ProdutoEntity produto = produtoRepository.findComMateriasPrimasById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: id=" + produtoId));

        return estruturaProdutoMapper.paraDetalhadoResponse(produto);
    }

    @Override
    @Transactional
    public ProdutoDetalhadoResponse atualizarEstrutura(Long produtoId, EstruturaProdutoAtualizacaoRequest request) {
        ProdutoEntity produto = produtoRepository.findComMateriasPrimasById(produtoId)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: id=" + produtoId));

        var ids = new HashSet<Long>();
        for (var item : request.itens()) {
            if (!ids.add(item.materiaPrimaId())) {
                throw new IllegalArgumentException(
                        "A matéria-prima id " + item.materiaPrimaId() + " foi informada mais de uma vez."
                );
            }
        }

        produto.getMateriasPrimas().clear();

        for (var item : request.itens()) {
            var materia = materiaPrimaRepository.findById(item.materiaPrimaId())
                    .orElseThrow(() -> new EntityNotFoundException("Matéria-prima não encontrada: id=" + item.materiaPrimaId()));

            var assoc = ProdutoMateriaPrimaEntity.builder()
                    .produto(produto)
                    .materiaPrima(materia)
                    .quantidadeNecessaria(item.quantidadeNecessaria())
                    .build();

            produto.getMateriasPrimas().add(assoc);
        }

        ProdutoEntity salvo = produtoRepository.save(produto);
        return estruturaProdutoMapper.paraDetalhadoResponse(salvo);
    }
}