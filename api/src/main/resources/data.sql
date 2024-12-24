-- Carga inicial tabela shedule_type
INSERT INTO schedule_type (name, days_worked, days_off, multiplier) VALUES
    ('Escala 1/3', 1, 3, 2),
    ('Escala 1/4', 1, 4, 2);

-- Carga inicial tabela position
INSERT INTO worker_position (name) VALUES
    ('subtenente'),
    ('sargento'),
    ('cabo'),
    ('soldado');