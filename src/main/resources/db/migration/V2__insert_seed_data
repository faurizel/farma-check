-- =============================================================
-- V2 - Massa de dados para testes
-- =============================================================

-- Unidade
INSERT INTO unidade (id, nome, cep, telefone, email, tipo_unidade, criacao, ultima_modificacao)
VALUES (nextval('unidade_sequence'), 'UBS Vila Mariana', '04101-300', '1155551234', 'ubsvilamariana@farmacheck.com', 'UBS', NOW(), NOW());

-- Medicamento
INSERT INTO medicamento (id, nome, principio_ativo, dosagem, laboratorio, criacao, ultima_modificacao)
VALUES (nextval('medicamento_sequence'), 'Paracetamol 500mg', 'Paracetamol', '500mg', 'EMS', NOW(), NOW());

-- Estoque
INSERT INTO estoque (id, quantidade, unidade_id, medicamento_id, criacao, ultima_modificacao)
VALUES (nextval('estoque_sequence'), 100, 1, 1, NOW(), NOW());

-- Usuario
INSERT INTO usuario (id, nome, email, senha, tipo_usuario, unidade_id, criacao, ultima_modificacao)
VALUES (nextval('usuario_sequence'), 'Admin Teste', 'admin@farmacheck.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMINISTRADOR', 1, NOW(), NOW());

-- Movimentacao
INSERT INTO movimentacao (id, quantidade, tipo_movimentacao, usuario_id, estoque_id, criacao, ultima_modificacao)
VALUES (nextval('movimentacao_sequence'), 100, 'ENTRADA', 1, 1, NOW(), NOW());
