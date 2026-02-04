package dev.fabiosimones.autoflex.services.impl;

import dev.fabiosimones.autoflex.dtos.response.SugestaoProducaoItemResponse;
import dev.fabiosimones.autoflex.dtos.response.SugestaoProducaoResponse;
import dev.fabiosimones.autoflex.entities.MateriaPrimaEntity;
import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import dev.fabiosimones.autoflex.repositories.MateriaPrimaRepository;
import dev.fabiosimones.autoflex.repositories.ProdutoRepository;
import dev.fabiosimones.autoflex.services.SugestaoProducaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SugestaoProducaoServiceImpl implements SugestaoProducaoService {

    private final ProdutoRepository produtoRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    @Override
    @Transactional(readOnly = true)
    public SugestaoProducaoResponse calcularSugestao() {

        List<ProdutoEntity> produtos = produtoRepository.findAll();
        produtos.sort(Comparator.comparing(ProdutoEntity::getValor, Comparator.nullsFirst(BigDecimal::compareTo)).reversed());

        Set<Long> idsMaterias = produtos.stream()
                .flatMap(p -> p.getMateriasPrimas().stream())
                .map(pm -> pm.getMateriaPrima().getId())
                .collect(Collectors.toSet());

        Map<Long, BigDecimal> estoque = new HashMap<>();
        if (!idsMaterias.isEmpty()) {
            List<MateriaPrimaEntity> materias = materiaPrimaRepository.findAllById(idsMaterias);
            for (MateriaPrimaEntity mp : materias) {
                estoque.put(mp.getId(), mp.getQuantidadeEstoque());
            }
        }

        List<SugestaoProducaoItemResponse> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ProdutoEntity produto : produtos) {
            if (produto.getMateriasPrimas() == null || produto.getMateriasPrimas().isEmpty()) {
                continue;
            }

            long maxUnidades = calcularMaxUnidades(produto, estoque);

            if (maxUnidades <= 0) {
                continue;
            }

            consumirEstoque(produto, estoque, maxUnidades);

            BigDecimal unidadesBD = BigDecimal.valueOf(maxUnidades);
            BigDecimal totalProduto = produto.getValor().multiply(unidadesBD);

            itens.add(new SugestaoProducaoItemResponse(
                    produto.getId(),
                    produto.getNome(),
                    produto.getValor(),
                    maxUnidades,
                    totalProduto
            ));

            valorTotal = valorTotal.add(totalProduto);
        }

        return new SugestaoProducaoResponse(itens, valorTotal);
    }

    private long calcularMaxUnidades(ProdutoEntity produto, Map<Long, BigDecimal> estoque) {
        BigDecimal min = null;

        for (var pm : produto.getMateriasPrimas()) {
            Long materiaId = pm.getMateriaPrima().getId();
            BigDecimal disponivel = estoque.getOrDefault(materiaId, BigDecimal.ZERO);
            BigDecimal necessario = pm.getQuantidadeNecessaria();

            if (necessario == null || necessario.compareTo(BigDecimal.ZERO) <= 0) {
                return 0;
            }

            BigDecimal possivel = disponivel.divide(necessario, 0, RoundingMode.FLOOR); // inteiro
            if (min == null || possivel.compareTo(min) < 0) {
                min = possivel;
            }
        }

        if (min == null) return 0;
        if (min.compareTo(BigDecimal.ZERO) <= 0) return 0;

        return min.longValue();
    }

    private void consumirEstoque(ProdutoEntity produto, Map<Long, BigDecimal> estoque, long unidades) {
        BigDecimal u = BigDecimal.valueOf(unidades);

        for (var pm : produto.getMateriasPrimas()) {
            Long materiaId = pm.getMateriaPrima().getId();
            BigDecimal disponivel = estoque.getOrDefault(materiaId, BigDecimal.ZERO);
            BigDecimal consumo = pm.getQuantidadeNecessaria().multiply(u);

            BigDecimal restante = disponivel.subtract(consumo);
            estoque.put(materiaId, restante.max(BigDecimal.ZERO));
        }
    }
}