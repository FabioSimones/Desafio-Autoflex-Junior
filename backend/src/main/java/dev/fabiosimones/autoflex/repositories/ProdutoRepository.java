package dev.fabiosimones.autoflex.repositories;

import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    @EntityGraph(attributePaths = {"materiasPrimas", "materiasPrimas.materiaPrima"})
    Optional<ProdutoEntity> findComMateriasPrimasById(Long id);
}
