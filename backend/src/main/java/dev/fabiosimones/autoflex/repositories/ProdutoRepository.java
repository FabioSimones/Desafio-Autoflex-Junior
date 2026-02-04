package dev.fabiosimones.autoflex.repositories;

import dev.fabiosimones.autoflex.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
}
