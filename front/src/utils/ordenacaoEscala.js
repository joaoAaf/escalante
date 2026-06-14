export const ordenarEscala = escala => {
    const escalaOrdenada = [...escala]
    escalaOrdenada.sort((servicoA, servicoB) => {
        const dataServicoA = servicoA?.dataServico ? new Date(servicoA.dataServico) : null
        const dataServicoB = servicoB?.dataServico ? new Date(servicoB.dataServico) : null

        if ((dataServicoA && dataServicoB) && (dataServicoA.getTime() !== dataServicoB.getTime()))
            return dataServicoA - dataServicoB
        if (dataServicoA && !dataServicoB) return -1
        if (!dataServicoA && dataServicoB) return 1

        const prioridadeFuncaoA = prioridadeFuncao(servicoA?.funcao)
        const prioridadeFuncaoB = prioridadeFuncao(servicoB?.funcao)
        if (prioridadeFuncaoA !== prioridadeFuncaoB)
            return prioridadeFuncaoA - prioridadeFuncaoB

        return 0
    })
    return escalaOrdenada
}

const prioridadeFuncao = funcao => {
    const prioridade = ordemFuncoes.indexOf(funcao)
    return prioridade === -1 ? ordemFuncoes.length : prioridade
}

const ordemFuncoes = [
    'Fiscal de Dia',
    'C.O.V.',
    'Operador de Linha 1',
    'Ajudante de Linha',
    'Permanente'
]