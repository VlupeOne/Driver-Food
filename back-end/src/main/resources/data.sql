-- =====================================================
-- Dados de Inicialização
-- =====================================================

-- =====================================================
-- CATEGORIAS
-- =====================================================

INSERT INTO categories (id, name, icon, color, type) VALUES
(1, 'Faturamento', 'money', '#4CAF50', 'RECEITA'),
(2, 'Gasolina', 'gas', '#FF9800', 'DESPESA'),
(3, 'Comida', 'food', '#E91E63', 'DESPESA'),
(4, 'Manutenção Moto', 'tools', '#2196F3', 'DESPESA'),
(5, 'Seguros e Taxas', 'document', '#9C27B0', 'DESPESA'),
(6, 'Uniforme e Equipamento', 'shirt', '#00BCD4', 'DESPESA');

-- =====================================================
-- CONTROLES DIÁRIOS
-- =====================================================

INSERT INTO daily_controls
(faturamento, gasolina, comida, date, recorded_at, observation)
VALUES
(150.00, 30.00, 25.50, '2024-07-01', '2024-07-01 18:00:00', 'Segunda produtiva');

INSERT INTO daily_controls
(faturamento, gasolina, comida, date, recorded_at, observation)
VALUES
(185.75, 35.00, 30.00, '2024-07-02', '2024-07-02 18:30:00', 'Movimento bom');

INSERT INTO daily_controls
(faturamento, gasolina, comida, date, recorded_at, observation)
VALUES
(210.50, 40.00, 28.00, '2024-07-03', '2024-07-03 19:00:00', 'Excelente dia');

INSERT INTO daily_controls
(faturamento, gasolina, comida, date, recorded_at, observation)
VALUES
(165.00, 32.50, 22.00, '2024-07-04', '2024-07-04 17:45:00', 'Dia normal');

INSERT INTO daily_controls
(faturamento, gasolina, comida, date, recorded_at, observation)
VALUES
(195.25, 38.00, 35.00, '2024-07-05', '2024-07-05 18:15:00', 'Sexta cheia');

-- =====================================================
-- DESPESAS EXTRAS
-- =====================================================

INSERT INTO daily_control_extras (daily_control_id, description, amount) VALUES
(1, 'Lavagem da moto', 15.00),
(1, 'Estacionamento', 5.00),

(2, 'Reparo de corrente', 20.00),
(2, 'Café', 7.50),

(3, 'Pneu novo', 80.00),
(3, 'Óleo do motor', 25.00),

(4, 'Bateria nova', 60.00),

(5, 'Limpeza profissional', 40.00),
(5, 'Ferramentas', 50.00),
(5, 'Uniforme', 35.00);