-- =============================================================
-- V1 - Criacao das tabelas do sistema FarmaCheck
-- =============================================================

-- Sequences
CREATE SEQUENCE IF NOT EXISTS unidade_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS medicamento_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS estoque_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS usuario_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS movimentacao_sequence START WITH 1 INCREMENT BY 1;

-- Tabela: unidade
CREATE TABLE unidade (
    id            INTEGER NOT NULL DEFAULT nextval('unidade_sequence'),
    nome          VARCHAR(255),
    cep           VARCHAR(255),
    telefone      VARCHAR(255),
    email         VARCHAR(255),
    tipo_unidade  VARCHAR(255),
    criacao             TIMESTAMP NOT NULL,
    ultima_modificacao  TIMESTAMP NOT NULL,
    CONSTRAINT pk_unidade PRIMARY KEY (id)
);

-- Tabela: medicamento
CREATE TABLE medicamento (
    id               INTEGER NOT NULL DEFAULT nextval('medicamento_sequence'),
    nome             VARCHAR(255),
    principio_ativo  VARCHAR(255),
    dosagem          VARCHAR(255),
    laboratorio      VARCHAR(255),
    criacao             TIMESTAMP NOT NULL,
    ultima_modificacao  TIMESTAMP NOT NULL,
    CONSTRAINT pk_medicamento PRIMARY KEY (id)
);

-- Tabela: estoque
CREATE TABLE estoque (
    id              INTEGER NOT NULL DEFAULT nextval('estoque_sequence'),
    quantidade      INTEGER,
    unidade_id      INTEGER,
    medicamento_id  INTEGER,
    criacao             TIMESTAMP NOT NULL,
    ultima_modificacao  TIMESTAMP NOT NULL,
    CONSTRAINT pk_estoque PRIMARY KEY (id),
    CONSTRAINT fk_estoque_unidade FOREIGN KEY (unidade_id) REFERENCES unidade (id),
    CONSTRAINT fk_estoque_medicamento FOREIGN KEY (medicamento_id) REFERENCES medicamento (id)
);

-- Tabela: usuario
CREATE TABLE usuario (
    id            INTEGER NOT NULL DEFAULT nextval('usuario_sequence'),
    nome          VARCHAR(255),
    email         VARCHAR(255) UNIQUE,
    senha         VARCHAR(255),
    tipo_usuario  VARCHAR(255),
    unidade_id    INTEGER,
    criacao             TIMESTAMP NOT NULL,
    ultima_modificacao  TIMESTAMP NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT fk_usuario_unidade FOREIGN KEY (unidade_id) REFERENCES unidade (id)
);

-- Tabela: movimentacao
CREATE TABLE movimentacao (
    id                  INTEGER NOT NULL DEFAULT nextval('movimentacao_sequence'),
    quantidade          INTEGER,
    tipo_movimentacao   VARCHAR(255),
    usuario_id          INTEGER,
    estoque_id          INTEGER,
    criacao             TIMESTAMP NOT NULL,
    ultima_modificacao  TIMESTAMP NOT NULL,
    CONSTRAINT pk_movimentacao PRIMARY KEY (id),
    CONSTRAINT fk_movimentacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_movimentacao_estoque FOREIGN KEY (estoque_id) REFERENCES estoque (id)
);
