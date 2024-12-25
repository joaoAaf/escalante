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