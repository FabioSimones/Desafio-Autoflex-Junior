package dev.fabiosimones.autoflex.services.impl;

import dev.fabiosimones.autoflex.dtos.response.SugestaoProducaoResponse;
import dev.fabiosimones.autoflex.entities.MateriaPrimaEntity;
import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import dev.fabiosimones.autoflex.entities.ProdutoMateriaPrimaEntity;
import dev.fabiosimones.autoflex.repositories.MateriaPrimaRepository;
import dev.fabiosimones.autoflex.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SugestaoProducaoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MateriaPrimaRepository materiaPrimaRepository;

    @InjectMocks
    private SugestaoProducaoServiceImpl service;

    @Test
    void devePriorizarProdutoDeMaiorValorEConsumirEstoqueCorretamente() {
        var mp1 = materiaPrima(1L, "MP1", bd("10.000"));
        var mp2 = materiaPrima(2L, "MP2", bd("5.000"));

        var produtoA = produto(1L, "Produto A", bd("200.00"),
                List.of(
                        assoc(mp1, bd("2.000")),
                        assoc(mp2, bd("1.000"))
                )
        );

        var produtoB = produto(2L, "Produto B", bd("150.00"),
                List.of(
                        assoc(mp1, bd("1.000")),
                        assoc(mp2, bd("1.000"))
                )
        );

        when(produtoRepository.findAll()).thenReturn(List.of(produtoB, produtoA)); // fora de ordem
        when(materiaPrimaRepository.findAllById(any())).thenReturn(List.of(mp1, mp2));

        SugestaoProducaoResponse resp = service.calcularSugestao();

        assertThat(resp.itens()).hasSize(1);
        assertThat(resp.itens().get(0).nomeProduto()).isEqualTo("Produto A");
        assertThat(resp.itens().get(0).quantidadeProduzivel()).isEqualTo(5L);
        assertThat(resp.itens().get(0).valorTotalProduto()).isEqualByComparingTo(bd("1000.00"));
        assertThat(resp.valorTotalProducao()).isEqualByComparingTo(bd("1000.00"));

        verify(produtoRepository, times(1)).findAll();
        verify(materiaPrimaRepository, times(1)).findAllById(any());
        verifyNoMoreInteractions(produtoRepository, materiaPrimaRepository);
    }

    @Test
    void deveIgnorarProdutoSemEstrutura() {
        var mp1 = materiaPrima(1L, "MP1", bd("10.000"));

        var produtoSemEstrutura = produto(10L, "Sem Estrutura", bd("999.99"), List.of());
        var produtoComEstrutura = produto(11L, "Com Estrutura", bd("10.00"),
                List.of(assoc(mp1, bd("2.000")))
        );

        when(produtoRepository.findAll()).thenReturn(List.of(produtoSemEstrutura, produtoComEstrutura));
        when(materiaPrimaRepository.findAllById(any())).thenReturn(List.of(mp1));

        SugestaoProducaoResponse resp = service.calcularSugestao();

        assertThat(resp.itens()).hasSize(1);
        assertThat(resp.itens().get(0).produtoId()).isEqualTo(11L);
        assertThat(resp.itens().get(0).quantidadeProduzivel()).isEqualTo(5L); // 10/2 = 5
        assertThat(resp.valorTotalProducao()).isEqualByComparingTo(bd("50.00"));
    }

    @Test
    void deveRetornarVazioQuandoNaoHaEstoqueSuficiente() {
        var mp1 = materiaPrima(1L, "MP1", bd("1.000"));

        var produto = produto(1L, "Produto", bd("100.00"),
                List.of(assoc(mp1, bd("2.000"))) // precisa de 2, tem 1
        );

        when(produtoRepository.findAll()).thenReturn(List.of(produto));
        when(materiaPrimaRepository.findAllById(any())).thenReturn(List.of(mp1));

        SugestaoProducaoResponse resp = service.calcularSugestao();

        assertThat(resp.itens()).isEmpty();
        assertThat(resp.valorTotalProducao()).isEqualByComparingTo(bd("0.00"));
    }

    @Test
    void deveDesempatarPorIdQuandoValorEmpata() {

        var mp1 = materiaPrima(1L, "MP1", bd("6.000"));
        var mp2 = materiaPrima(2L, "MP2", bd("2.000"));

        var p1 = produto(1L, "Produto 1", bd("100.00"),
                List.of(
                        assoc(mp1, bd("2.000")),
                        assoc(mp2, bd("1.000"))
                )
        );

        var p2 = produto(2L, "Produto 2", bd("100.00"),
                List.of(
                        assoc(mp1, bd("2.000"))
                )
        );

        when(produtoRepository.findAll()).thenReturn(List.of(p2, p1));
        when(materiaPrimaRepository.findAllById(any())).thenReturn(List.of(mp1, mp2));

        var resp = service.calcularSugestao();

        assertThat(resp.itens()).hasSize(2);

        // Desempate por ID (mesmo valor): p1 antes de p2
        assertThat(resp.itens().get(0).produtoId()).isEqualTo(1L);
        assertThat(resp.itens().get(1).produtoId()).isEqualTo(2L);

        assertThat(resp.itens().get(0).quantidadeProduzivel()).isEqualTo(2L);
        assertThat(resp.itens().get(1).quantidadeProduzivel()).isEqualTo(1L);

        assertThat(resp.valorTotalProducao()).isEqualByComparingTo(bd("300.00"));
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeNecessariaForZeroOuNegativa() {
        var mp1 = materiaPrima(1L, "MP1", bd("10.000"));

        var produtoInvalido = produto(99L, "Produto Inválido", bd("100.00"),
                List.of(assoc(mp1, bd("0.000"))) // inválido
        );

        when(produtoRepository.findAll()).thenReturn(List.of(produtoInvalido));
        when(materiaPrimaRepository.findAllById(any())).thenReturn(List.of(mp1));

        assertThatThrownBy(() -> service.calcularSugestao())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantidade necessária deve ser maior que zero");
    }

    private static BigDecimal bd(String v) {
        return new BigDecimal(v);
    }

    private static MateriaPrimaEntity materiaPrima(Long id, String nome, BigDecimal estoque) {
        var mp = new MateriaPrimaEntity();
        mp.setId(id);
        mp.setNome(nome);
        mp.setQuantidadeEstoque(estoque);
        return mp;
    }

    private static ProdutoEntity produto(Long id, String nome, BigDecimal valor, List<ProdutoMateriaPrimaEntity> estrutura) {
        var p = new ProdutoEntity();
        p.setId(id);
        p.setNome(nome);
        p.setValor(valor);

        for (var pm : estrutura) {
            pm.setProduto(p);
            p.getMateriasPrimas().add(pm);
        }

        return p;
    }

    private static ProdutoMateriaPrimaEntity assoc(MateriaPrimaEntity materiaPrima, BigDecimal quantidadeNecessaria) {
        var pm = new ProdutoMateriaPrimaEntity();
        pm.setMateriaPrima(materiaPrima);
        pm.setQuantidadeNecessaria(quantidadeNecessaria);
        return pm;
    }
}