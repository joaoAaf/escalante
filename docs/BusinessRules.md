### Descrição da Necessidade:
Automatizar a tarefa de criação de escalas mensais de trabalho para militares do CBMCE. Atuamante esta tarefa é realizada de forma manual e consome um tempo excessivamente grande dos setores responsáveis.

### Regras de Negócio:
1. Os trabalhadores que comporem a escala devem trabalhar no regime de 1/3 (1 dia de trabalho para 3 dias de folga) ou 1/4 (1 dia de trabalho para 4 dias de folga) caso estes exerção a função de `fiscal` ou `motorista`;

1. A escala deve permitir que os dias de trabalho/folgas possam ser aumentados proporcionalmente, por exemplo 1/3 --> 2/6 --> 3/9;

1. Cada dia de trabalho deverá haver 01 equipe com pelo menos 05 trabalhadores, 01 para cada função;

1. As equipes serão divididas em 5 funções, estas serão distribuidas aos trabalhadores escalados de acordo com seus cargos, conforme as tabelas abaixo:

    #### FUNÇÕES X CARGOS

    Função | Cargos
    --- | ---
    Fiscal | Suboficial, Sargento ou Cabo
    Chefe de Linha | Cabo ou Soldado
    Auxiliar de Linha | Soldado ou Cabo
    Permanente | Soldado ou Cabo
    Motorista | Qualquer trabalhador autorizado

    #### PRIORIDADES PARA ASSUMIR FUNÇÕES

    Cargos/ Funções | Fiscal | Chefe de Linha | Auxiliar de Linha | Permanente | Motorista
    --- | --- | --- | --- | --- | ---
    Suboficial | 1º | - | - | - | -
    Sargento | 1º | - | - | - | -
    Cabo | 2º | 1º | 2º | 2º | -
    Soldado | - | 2º | 1º | 1º | -

1. Um trabalhador que assumir a função de `fiscal` também pode assumir a de `motorista`, neste caso deverão haver 02 `auxiliares de linha`;

1. A escala deve considerar as ausencias legais, como férias, licenças de saúde, capacitação, dentre outras.