export const ordenarEscala = escala => {
    const escalaOrdenada = [...escala]
    escalaOrdenada.sort((servicoA, servicoB) => {
        const dataServicoA = servicoA?.dataServico ? new Date(servicoA.dataServico) : null
        const dataServicoB = servicoB?.dataServico ? new Date(servicoB.dataServico) : null

        if ((dataServicoA && dataServicoB) && (dataServicoA.getTime() !== dataServicoB.getTime()))
            return dataServicoA - dataServicoB
        if (dataServicoA && !dataServicoB) return -1
        if (!dataServicoA && dataServicoB) return 1

        const prioridadeFuncaoA = prioridadeFuncao(servicoA)
        const prioridadeFuncaoB = prioridadeFuncao(servicoB)
        if (prioridadeFuncaoA !== prioridadeFuncaoB)
            return prioridadeFuncaoA - prioridadeFuncaoB

        if (servicoA?.funcao === 'Operador de Linha' && servicoB?.funcao === 'Operador de Linha') {
            const linhaA = servicoA.linha ?? 1
            const linhaB = servicoB.linha ?? 1
            return linhaA - linhaB
        }

        return 0
    })
    return escalaOrdenada
}

const prioridadeFuncao = servico => {
    const funcao = servico?.funcao
    const chave = funcao === 'Operador de Linha'
        ? (servico?.linha === 2 ? 'Operador de Linha 2' : 'Operador de Linha 1')
        : funcao
    const prioridade = ordemFuncoes.indexOf(chave)
    return prioridade === -1 ? ordemFuncoes.length : prioridade
}

const ordemFuncoes = [
    'Fiscal de Dia',
    'C.O.V.',
    'Operador de Linha 1',
    'Ajudante de Linha',
    'Operador de Linha 2',
    'Permanente'
]
