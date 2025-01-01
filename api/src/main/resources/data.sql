-- Carga inicial tabela shedule_type
INSERT INTO schedule_type (name, days_worked, days_off, multiplier) VALUES
    ('escala 1/3', 1, 3, 2),
    ('escala 1/4', 1, 4, 2),
    ('escala 1/7', 1, 7, 2);

-- Carga inicial tabela position
INSERT INTO worker_position (name, seniority) VALUES
    ('tenente', 1),
    ('subtenente', 2),
    ('sargento', 3),
    ('cabo', 4),
    ('soldado', 5);

--  Carga inicial tabela worker_role
INSERT INTO worker_role (name, id_schedule_type) VALUES
    ('fiscal', 2),
    ('chefe de linha', 1),
    ('auxiliar de linha', 1),
    ('permanente', 1),
    ('motorista', 2);

--  Carga inicial tabela worker_priority
INSERT INTO worker_priority (id_worker_role, id_worker_position, priority) VALUES
    -- Fiscal/ Tenente
    (1, 1, 1),
    -- Fiscal/ Subtenente
    (1, 2, 2),
    -- Fiscal/ Sargento
    (1, 3, 3),
    -- Fiscal/ Cabo
    (1, 4, 4),
    -- Fiscal/ Soldado
    (1, 5, 5),
    -- Chefe de Linha/ Subtenente
    (2, 2, 3),
    -- Chefe de Linha/ Sargento
    (2, 3, 2),
    -- Chefe de Linha/ Cabo
    (2, 4, 1),
    -- Chefe de Linha/ Soldado
    (2, 5, 4),
    -- Auxiliar de Linha/ Subtenente
    (3, 2, 4),
    -- Auxiliar de Linha/ Sargento
    (3, 3, 3),
    -- Auxiliar de Linha/ Cabo
    (3, 4, 1),
    -- Auxiliar de Linha/ Soldado
    (3, 5, 2),
    -- Permanente/ Subtenente
    (4, 2, 4),
    -- Permanente/ Sargento
    (4, 3, 3),
    -- Permanente/ Cabo
    (4, 4, 2),
    -- Permanente/ Soldado
    (4, 5, 1);

-- Carga inicial da tabela de trabalhadores
INSERT INTO worker (
    registration, name, sex, seniority, phone, email, birthdate, 
    driver, scheduleable, id_worker_position, id_schedule_type
)
VALUES
-- Tenente (1 trabalhador)
('REG874', 'Carlos Silva', 'M', 1, '1234567890', 'tenente@example.com', '1980-01-01', FALSE, TRUE, 1, NULL),

-- Subtenentes (2 trabalhadores)
('REG532', 'Ana Oliveira', 'F', 2, '1234567891', 'subtenente1@example.com', '1982-02-02', FALSE, TRUE, 2, NULL),
('REG687', 'João Souza', 'M', 3, '1234567892', 'subtenente2@example.com', '1984-03-03', FALSE, TRUE, 2, NULL),

-- Sargentos (4 trabalhadores)
('REG920', 'Paulo Lima', 'M', 4, '1234567893', 'sargento1@example.com', '1986-04-04', TRUE, TRUE, 3, NULL),
('REG385', 'Mariana Costa', 'F', 5, '1234567894', 'sargento2@example.com', '1988-05-05', TRUE, TRUE, 3, NULL),
('REG741', 'Rafael Mendes', 'M', 6, '1234567895', 'sargento3@example.com', '1990-06-06', FALSE, TRUE, 3, NULL),
('REG829', 'Camila Rocha', 'F', 7, '1234567896', 'sargento4@example.com', '1992-07-07', FALSE, TRUE, 3, NULL),

-- Cabos (13 trabalhadores)
('REG273', 'Roberto Almeida', 'M', 8, '1234567897', 'cabo1@example.com', '1994-08-08', TRUE, TRUE, 4, NULL),
('REG104', 'Fernanda Santos', 'F', 9, '1234567898', 'cabo2@example.com', '1996-09-09', TRUE, TRUE, 4, NULL),
('REG672', 'André Carvalho', 'M', 10, '1234567899', 'cabo3@example.com', '1998-10-10', TRUE, TRUE, 4, NULL),
('REG835', 'Letícia Pereira', 'F', 11, '2234567890', 'cabo4@example.com', '2000-11-11', FALSE, TRUE, 4, NULL),
('REG329', 'Bruno Martins', 'M', 12, '2234567891', 'cabo5@example.com', '2002-12-12', FALSE, TRUE, 4, NULL),
('REG450', 'Carla Vieira', 'F', 13, '2234567892', 'cabo6@example.com', '2004-01-13', FALSE, TRUE, 4, NULL),
('REG582', 'Lucas Borges', 'M', 14, '2234567893', 'cabo7@example.com', '2006-02-14', FALSE, TRUE, 4, 3),
('REG193', 'Juliana Barros', 'F', 15, '2234567894', 'cabo8@example.com', '2008-03-15', FALSE, TRUE, 4, NULL),
('REG405', 'Marcos Tavares', 'M', 16, '2234567895', 'cabo9@example.com', '1994-03-01', FALSE, TRUE, 4, NULL),
('REG764', 'Alice Nunes', 'F', 17, '2234567896', 'cabo10@example.com', '1995-02-11', FALSE, TRUE, 4, NULL),
('REG328', 'João Pinto', 'M', 18, '2234567897', 'cabo11@example.com', '1997-06-23', FALSE, TRUE, 4, NULL),
('REG412', 'Sofia Melo', 'F', 19, '2234567898', 'cabo12@example.com', '1999-09-12', FALSE, FALSE, 4, NULL),
('REG713', 'Henrique Lemos', 'M', 20, '2234567899', 'cabo13@example.com', '2001-01-17', FALSE, TRUE, 4, NULL),

-- Soldados (8 trabalhadores)
('REG418', 'Pedro Silva', 'M', 21, '3234567890', 'soldado1@example.com', '1990-04-16', TRUE, TRUE, 5, NULL),
('REG294', 'Joana Ferreira', 'F', 22, '3234567891', 'soldado2@example.com', '1991-05-17', TRUE, TRUE, 5, NULL),
('REG561', 'Gustavo Ramos', 'M', 23, '3234567892', 'soldado3@example.com', '1992-06-18', FALSE, TRUE, 5, NULL),
('REG873', 'Adriana Lima', 'F', 24, '3234567893', 'soldado4@example.com', '1993-07-19', FALSE, TRUE, 5, NULL),
('REG690', 'Felipe Castro', 'M', 25, '3234567894', 'soldado5@example.com', '1994-08-20', FALSE, TRUE, 5, NULL),
('REG234', 'Aline Monteiro', 'F', 26, '3234567895', 'soldado6@example.com', '1995-09-21', FALSE, TRUE, 5, NULL),
('REG348', 'Thiago Gomes', 'M', 27, '3234567896', 'soldado7@example.com', '1996-10-22', FALSE, FALSE, 5, NULL),
('REG712', 'Bianca Farias', 'F', 28, '3234567897', 'soldado8@example.com', '1997-11-23', FALSE, TRUE, 5, NULL);

-- Carga inicial de ausencias
INSERT INTO worker_absence (start_date, end_date, reason, id_worker) VALUES
    ('2024-09-01', '2024-09-30', 'ferias', 5);