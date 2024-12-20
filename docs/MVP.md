## Sistema para criação de escala de serviço
### Estrutura
- Modelo (Model):
    1. Trabalhador (Worker);
        * Nome (name);
        * Matrícula (registrationNumber);
        * Cargo (position);
        * Telefone (tel);
        * Data de Nascimento (birthdate);
        * Data do Ultimo dia trabalhado (lastDayWorked);
        * Motorista (driver);
        * Tipo de escala (scheduleType);
        * Escalavel (scheduleable);

    1. Trabalhador Escalado (ScheduledWorker);
        * trabalhador (worker);
        * Data que está escalado (dateScheduled);
        * Função (role);

    1. Ausências (absences);
        * trabalhador (worker);
        * Inicio da ausência (startDate);
        * Fim da ausência (endDate);
        * Tipo da ausência (absenceType);
        * Descrição da ausência (absenceDescription);

    1. Cargo (position);
        * Nome do Cargo (name);
        * Tipo de escala (scheduleType);

    1. Função (Role);
        * Nome da Função (name);
        * Quantidade miníma de trabalhadores (MinimumQuantity);
        * Tipo de escala (scheduleType);

    1. Tipo de escala (ScheduleType);
        * Nome do Tipo (name);
        * Quantidade de dias trabalhados (daysWorked);
        * Quantidade de dias de folga (daysOff);
        * Multiplicador (multiplier);