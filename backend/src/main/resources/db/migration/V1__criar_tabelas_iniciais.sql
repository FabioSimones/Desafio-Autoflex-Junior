CREATE TABLE produtos (
  id BIGINT PRIMARY KEY NOT NULL UNIQUE,
  nome VARCHAR(120) NOT NULL,
  valor NUMERIC(15,2) NOT NULL
);

CREATE TABLE materias_primas (
  id BIGINT PRIMARY KEY NOT NULL UNIQUE,
  nome VARCHAR(120) NOT NULL,
  quantidade_estoque NUMERIC(15,3) NOT NULL
);

CREATE TABLE produtos_materias_primas (
  id BIGINT PRIMARY KEY,
  produto_id BIGINT NOT NULL,
  materia_prima_id BIGINT NOT NULL,
  quantidade_necessaria NUMERIC(15,3) NOT NULL,
  CONSTRAINT fk_pmp_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
  CONSTRAINT fk_pmp_materia FOREIGN KEY (materia_prima_id) REFERENCES materias_primas(id),
  CONSTRAINT uq_produto_materia UNIQUE (produto_id, materia_prima_id)
);

CREATE INDEX idx_pmp_produto ON produtos_materias_primas(produto_id);
CREATE INDEX idx_pmp_materia ON produtos_materias_primas(materia_prima_id);
