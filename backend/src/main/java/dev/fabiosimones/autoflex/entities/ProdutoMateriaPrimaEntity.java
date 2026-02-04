package dev.fabiosimones.autoflex.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "produtos_materias_primas",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_produto_materia", columnNames = {"produto_id", "materia_prima_id"})
        },
        indexes = {
                @Index(name = "idx_pmp_produto", columnList = "produto_id"),
                @Index(name = "idx_pmp_materia", columnList = "materia_prima_id")
        }
)
public class ProdutoMateriaPrimaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutoEntity produto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_prima_id", nullable = false)
    private MateriaPrimaEntity materiaPrima;

    @Column(name = "quantidade_necessaria", nullable = false, precision = 15, scale = 3)
    private BigDecimal quantidadeNecessaria;
}
