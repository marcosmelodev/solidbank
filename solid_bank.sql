-- ==========================================
-- SOLID BANK - SCRIPT DE CRIAÇÃO DO BANCO
-- MySQL Workbench
-- ==========================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS solid_bank;
USE solid_bank;

-- ==========================================
-- TABELA: cliente
-- Armazena informações dos clientes do banco
-- ==========================================
CREATE TABLE cliente (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    cpf_cnpj VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefone VARCHAR(20),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    INDEX idx_cpf_cnpj (cpf_cnpj),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================================
-- TABELA: conta
-- Armazena as contas bancárias
-- Cada cliente pode ter no máximo uma conta de cada tipo
-- ==========================================
CREATE TABLE conta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero_conta VARCHAR(20) NOT NULL UNIQUE,
    cliente_id BIGINT NOT NULL,
    tipo_conta ENUM('CORRENTE', 'POUPANCA', 'JURIDICA') NOT NULL,
    saldo DECIMAL(15, 2) DEFAULT 0.00,
    data_abertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativa BOOLEAN DEFAULT TRUE,
    
    -- Chave estrangeira
    CONSTRAINT fk_conta_cliente 
        FOREIGN KEY (cliente_id) 
        REFERENCES cliente(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    
    -- Garantir que cliente tenha apenas uma conta de cada tipo
    CONSTRAINT uk_cliente_tipo 
        UNIQUE (cliente_id, tipo_conta),
    
    -- Índices para performance
    INDEX idx_numero_conta (numero_conta),
    INDEX idx_cliente_id (cliente_id),
    INDEX idx_tipo_conta (tipo_conta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================================
-- TABELA: transacao
-- Registra todas as transações realizadas
-- ==========================================
CREATE TABLE transacao (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conta_origem_id BIGINT,
    conta_destino_id BIGINT,
    tipo_transacao ENUM('DEPOSITO', 'SAQUE', 'TRANSFERENCIA') NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    data_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descricao VARCHAR(255),
    
    -- Chaves estrangeiras
    CONSTRAINT fk_transacao_conta_origem 
        FOREIGN KEY (conta_origem_id) 
        REFERENCES conta(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    
    CONSTRAINT fk_transacao_conta_destino 
        FOREIGN KEY (conta_destino_id) 
        REFERENCES conta(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    
    -- Validações
    CONSTRAINT chk_valor_positivo 
        CHECK (valor > 0),
    
    -- Índices
    INDEX idx_conta_origem (conta_origem_id),
    INDEX idx_conta_destino (conta_destino_id),
    INDEX idx_data_transacao (data_transacao),
    INDEX idx_tipo_transacao (tipo_transacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================================
-- DADOS DE EXEMPLO (OPCIONAL)
-- ==========================================

-- Inserir clientes de exemplo
INSERT INTO cliente (nome, cpf_cnpj, email, telefone) VALUES
('João Silva', '12345678901', 'joao.silva@email.com', '11987654321'),
('Maria Santos', '98765432109', 'maria.santos@email.com', '11876543210'),
('Empresa XYZ Ltda', '12345678000199', 'contato@empresaxyz.com', '1133334444');

-- Inserir contas de exemplo
INSERT INTO conta (numero_conta, cliente_id, tipo_conta, saldo) VALUES
('CC0001', 1, 'CORRENTE', 1000.00),
('CP0001', 1, 'POUPANCA', 5000.00),
('CC0002', 2, 'CORRENTE', 2500.00),
('CJ0001', 3, 'JURIDICA', 50000.00);

-- Inserir transações de exemplo
INSERT INTO transacao (conta_origem_id, conta_destino_id, tipo_transacao, valor, descricao) VALUES
(NULL, 1, 'DEPOSITO', 1000.00, 'Depósito inicial'),
(NULL, 2, 'DEPOSITO', 5000.00, 'Depósito inicial'),
(1, 3, 'TRANSFERENCIA', 500.00, 'Transferência entre contas');

-- ==========================================
-- VIEWS ÚTEIS
-- ==========================================

-- View: Visão geral de clientes com suas contas
CREATE VIEW vw_clientes_contas AS
SELECT 
    c.id AS cliente_id,
    c.nome AS cliente_nome,
    c.cpf_cnpj,
    co.id AS conta_id,
    co.numero_conta,
    co.tipo_conta,
    co.saldo,
    co.data_abertura
FROM cliente c
LEFT JOIN conta co ON c.id = co.cliente_id
WHERE c.ativo = TRUE AND (co.ativa = TRUE OR co.ativa IS NULL);

-- View: Extrato completo de transações
CREATE VIEW vw_extrato_transacoes AS
SELECT 
    t.id AS transacao_id,
    t.tipo_transacao,
    t.valor,
    t.data_transacao,
    t.descricao,
    co.numero_conta AS conta_origem,
    cd.numero_conta AS conta_destino,
    COALESCE(co.cliente_id, cd.cliente_id) AS cliente_id
FROM transacao t
LEFT JOIN conta co ON t.conta_origem_id = co.id
LEFT JOIN conta cd ON t.conta_destino_id = cd.id
ORDER BY t.data_transacao DESC;

-- ==========================================
-- PROCEDURES ÚTEIS
-- ==========================================

-- Procedure: Realizar transferência entre contas
DELIMITER //
CREATE PROCEDURE sp_transferir(
    IN p_conta_origem_id BIGINT,
    IN p_conta_destino_id BIGINT,
    IN p_valor DECIMAL(15, 2),
    IN p_descricao VARCHAR(255),
    OUT p_sucesso BOOLEAN,
    OUT p_mensagem VARCHAR(255)
)
BEGIN
    DECLARE v_saldo_origem DECIMAL(15, 2);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_sucesso = FALSE;
        SET p_mensagem = 'Erro ao realizar transferência';
    END;
    
    START TRANSACTION;
    
    -- Verificar saldo da conta origem
    SELECT saldo INTO v_saldo_origem 
    FROM conta 
    WHERE id = p_conta_origem_id AND ativa = TRUE
    FOR UPDATE;
    
    IF v_saldo_origem < p_valor THEN
        SET p_sucesso = FALSE;
        SET p_mensagem = 'Saldo insuficiente';
        ROLLBACK;
    ELSE
        -- Debitar da conta origem
        UPDATE conta 
        SET saldo = saldo - p_valor 
        WHERE id = p_conta_origem_id;
        
        -- Creditar na conta destino
        UPDATE conta 
        SET saldo = saldo + p_valor 
        WHERE id = p_conta_destino_id;
        
        -- Registrar transação
        INSERT INTO transacao (conta_origem_id, conta_destino_id, tipo_transacao, valor, descricao)
        VALUES (p_conta_origem_id, p_conta_destino_id, 'TRANSFERENCIA', p_valor, p_descricao);
        
        SET p_sucesso = TRUE;
        SET p_mensagem = 'Transferência realizada com sucesso';
        COMMIT;
    END IF;
END //
DELIMITER ;

-- ==========================================
-- TRIGGERS
-- ==========================================

-- Trigger: Validar saldo antes de saque
DELIMITER //
CREATE TRIGGER trg_validar_saldo_antes_saque
BEFORE UPDATE ON conta
FOR EACH ROW
BEGIN
    IF NEW.saldo < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Saldo não pode ser negativo';
    END IF;
END //
DELIMITER ;

-- ==========================================
-- CONSULTAS ÚTEIS PARA TESTES
-- ==========================================

-- Listar todos os clientes com suas contas
SELECT * FROM vw_clientes_contas;

-- Ver extrato de transações
SELECT * FROM vw_extrato_transacoes;

-- Consultar saldo de uma conta específica
SELECT numero_conta, tipo_conta, saldo 
FROM conta 
WHERE numero_conta = 'CC0001';

-- Listar transações de um cliente específico
SELECT 
    t.data_transacao,
    t.tipo_transacao,
    t.valor,
    t.descricao
FROM transacao t
LEFT JOIN conta co ON t.conta_origem_id = co.id
LEFT JOIN conta cd ON t.conta_destino_id = cd.id
WHERE co.cliente_id = 1 OR cd.cliente_id = 1
ORDER BY t.data_transacao DESC;
