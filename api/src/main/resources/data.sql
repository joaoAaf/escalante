-- Carga inicial tabela shedule_type
INSERT INTO schedule_type (name, days_worked, days_off, multiplier) VALUES
    ('escala 1/3', 1, 3, 2),
    ('escala 1/4', 1, 4, 2);

-- Carga inicial tabela position
INSERT INTO worker_position (name) VALUES
    ('subtenente'),
    ('sargento'),
    ('cabo'),
    ('soldado');

--  Carga inicial tabela worker_role
INSERT INTO worker_role (name, id_schedule_type) VALUES
    ('fiscal', 2),
    ('chefe de linha', 1),
    ('auxiliar de linha', 1),
    ('permanente', 1),
    ('motorista', 2);

--  Carga inicial tabela worker_priority
INSERT INTO worker_priority (id_worker_role, id_worker_position, priority) VALUES
    (1, 1, 1),
    (1, 2, 2),
    (1, 3, 3),
    (1, 4, 4),
    (2, 1, 3),
    (2, 2, 2),
    (2, 3, 1),
    (2, 4, 4),
    (3, 1, 4),
    (3, 2, 3),
    (3, 3, 1),
    (3, 4, 2),
    (4, 1, 4),
    (4, 2, 3),
    (4, 3, 2),
    (4, 4, 1);