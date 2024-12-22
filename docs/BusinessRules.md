### Descrição da Necessidade:
Automatizar a tarefa de criação de escalas mensais de trabalho para militares do CBMCE. Atuamante esta tarefa é realizada de forma manual e consome um tempo excessivamente grande dos setores responsáveis.

### Regras de Negócio:
1. Os trabalhadores que comporem a escala devem trabalhar no regime de 1/3 (1 dia de trabalho para 3 dias de folga) ou 1/4 (1 dia de trabalho para 4 dias de folga), caso estes exerção a função de `fiscal` ou `motorista`;

1. A escala deve permitir que os dias de trabalho/folgas possam ser aumentados proporcionalmente, por exemplo 1/3 --> 2/6 --> 3/9;

1. A escala deve permitir selecionar tanto trabalhadores como cargos para entrar em regimes de trabalho especiais;

1. Cada dia de trabalho deverá haver 01 equipe com pelo menos 05 trabalhadores, 01 para cada função;

1. As equipes serão divididas em 5 funções, estas serão distribuidas aos trabalhadores escalados de acordo com seus cargos, conforme as tabelas abaixo:

    #### PRIORIDADES PARA ASSUMIR FUNÇÕES

    Funções/ Cargos | Subtenente | Sargento | Cabo | Soldado
    --- | --- | --- | --- | ---
    Fiscal | 1º | 2º | 3º | 4º
    Chefe de Linha | 3º | 2º | 1º | 4º
    Auxiliar de Linha | 4º | 3º | 1º | 2º
    Permanente | 4º | 3º | 2º | 1º
    Motorista | - | - | - | -

    Obs: Qualquer trabalhador poderá assumir a função de `motorista`, desde que esteja autorizado a exerce-la.

1. No momento da seleção dos trabalhadores para cada função deverá ser levada em conta a antiguidade do trabalhador. A seleção das funções será feita na seguinte ordem:
    - 1º Motorista: Os mais antigos serão escolhidos primeiro;
    - 2º Permanente: Os mais modernos serão escolhidos primeiro;
    - 3º Auxiliar de Linha: Os mais modernos serão escolhidos primeiro;
    - 4º Chefe de Linha: Os mais modernos serão escolhidos primeiro;
    - 5º Fiscal: Os mais antigos serão escolhidos primeiro.

1. Além das regras definidas acima, deve-se escalar primeiro os trabalhadores que estiverem a mais tempo fora da escala;

1. Os trabalhadores autorizados a exercerem a função de `motorista` serão os ultimos a serem escolhidos para as demais funções, com excessão da função de `fiscal`.

1. Um trabalhador que assumir a função de `fiscal` também pode assumir a de `motorista`, neste caso deverão haver 02 `chefes de linha`;

1. A escala deve considerar as ausencias legais, como férias, licenças de saúde, capacitação, dentre outras.