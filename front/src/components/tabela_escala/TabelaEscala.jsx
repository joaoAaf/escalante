import {useState, useMemo, useCallback} from 'react'
import {ordenarEscala} from '../../utils/ordenacaoEscala'
import ModalEscala from './ModalEscala'
import Styles from './styles.module.css'

const CORES = ['#e3f2fd', '#ffe0b2', '#c8e6c9', '#fff9c4']
const DIAS_SEMANA = ['SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO']

const LABEL_FUNCAO = {
    'Fiscal de Dia': 'OFICIAL/FISCAL DE DIA',
    'C.O.V.': 'C.O.V – ABT',
    'Operador de Linha 1': 'OP. LINHA 01/ SOCORR',
    'Ajudante de Linha': 'AJUD. L 1/SOCORR.',
    'Operador de Linha 2': 'OP. LINHA 02/ SOCORR',
    'Permanente': 'PERMANENTE'
}

const ORDEM_LINHAS = [
    'Fiscal de Dia',
    'C.O.V.',
    'Operador de Linha 1',
    'Ajudante de Linha',
    'Operador de Linha 2',
    'Permanente'
]

const LINHA_POR_CHAVE = {
    'Operador de Linha 1': 1,
    'Operador de Linha 2': 2
}

function normalize(str) {
    return String(str ?? '').toLowerCase().trim()
}

function formatarDataDia(date) {
    const meses = [
        'janeiro', 'fevereiro', 'março', 'abril', 'maio', 'junho',
        'julho', 'agosto', 'setembro', 'outubro', 'novembro', 'dezembro'
    ]
    return `${date.getDate()}. ${meses[date.getMonth()]} ${date.getFullYear()}`
}

function montarGradeSemanal(escalaOrdenada) {
    if (!escalaOrdenada || escalaOrdenada.length === 0) return []

    const mapa = new Map()

    escalaOrdenada.forEach(servico => {
        if (!servico?.dataServico) return
        const date = new Date(servico.dataServico + 'T00:00:00')
        let diaSemana = date.getDay()
        diaSemana = diaSemana === 0 ? 6 : diaSemana - 1

        const inicioSemana = new Date(date)
        const dia = inicioSemana.getDay()
        const diff = dia === 0 ? -6 : 1 - dia
        inicioSemana.setDate(inicioSemana.getDate() + diff)
        const chaveSemana = inicioSemana.toISOString().split('T')[0]

        if (!mapa.has(chaveSemana)) {
            mapa.set(chaveSemana, {
                inicio: new Date(inicioSemana),
                servicos: {}
            })
        }

        const semana = mapa.get(chaveSemana)
        const chaveLinha = obterChaveLinha(servico)
        if (!chaveLinha) return

        if (!semana.servicos[chaveLinha]) {
            semana.servicos[chaveLinha] = Array(7).fill(null)
        }
        semana.servicos[chaveLinha][diaSemana] = servico
    })

    const semanas = []
    for (const [chave, dados] of mapa) {
        semanas.push({
            chave,
            inicio: dados.inicio,
            servicos: dados.servicos
        })
    }

    return semanas.sort((a, b) => a.inicio - b.inicio)
}

function obterChaveLinha(servico) {
    if (servico.funcao === 'Operador de Linha') {
        if (servico.linha === 2) return 'Operador de Linha 2'
        return 'Operador de Linha 1'
    }
    return servico.funcao
}

export default function TabelaEscala({escalaTabela, busca}) {
    const [modalServico, setModalServico] = useState(null)

    const gradeSemanal = useMemo(() => {
        const escala = escalaTabela || []
        const escalaOrdenada = ordenarEscala(escala)
        return montarGradeSemanal(escalaOrdenada)
    }, [escalaTabela])

    const queryBusca = useMemo(() => {
        if (!busca || !busca.consulta) return null
        return {
            campo: busca.campo,
            termo: normalize(busca.consulta)
        }
    }, [busca])

    const isDestacado = useCallback((servico, funcao) => {
        if (!queryBusca) return true
        const {campo, termo} = queryBusca

        if (campo === 'militar' && servico) {
            return normalize(servico.nomePaz).includes(termo) ||
                   normalize(servico.patente).includes(termo)
        }
        if (campo === 'funcao' && funcao) {
            return normalize(funcao).includes(termo)
        }
        if (campo === 'data' && termo && servico) {
            return normalize(servico.dataServico).includes(termo)
        }
        if (campo === 'matricula' && servico) {
            return normalize(servico.matricula).includes(termo)
        }
        return true
    }, [queryBusca])

    const corClassMap = useMemo(() => ({
        '#e3f2fd': Styles['bg-blue'],
        '#ffe0b2': Styles['bg-orange'],
        '#c8e6c9': Styles['bg-green'],
        '#fff9c4': Styles['bg-yellow'],
        '#eeeeee': Styles['bg-grey']
    }), [])

    const gradeComCores = useMemo(() => {
        if (!gradeSemanal.length) return []

        return gradeSemanal.map(semana => {
            let primeiroDiaServico = 0
            for (let i = 0; i < 7; i++) {
                const temServico = ORDEM_LINHAS.some(funcao =>
                    semana.servicos[funcao]?.[i] != null
                )
                if (temServico) {
                    primeiroDiaServico = i
                    break
                }
            }

            return {
                ...semana,
                coresPorDia: Array.from({length: 7}, (_, diaIdx) => {
                    if (diaIdx < primeiroDiaServico) return '#eeeeee'
                    const diffDias = diaIdx - primeiroDiaServico
                    const corIdx = Math.floor(diffDias / 2) % 4
                    return CORES[corIdx]
                })
            }
        })
    }, [gradeSemanal])

    if (!escalaTabela || escalaTabela.length === 0) {
        return (
            <div>
                <p>Nenhum serviço encontrado. Tente criar uma nova escala.</p>
            </div>
        )
    }

    return (
        <>
            {gradeComCores.map(semana => (
                <table key={semana.chave} className={Styles['weekly-table']}>
                    <thead>
                        <tr>
                            <th></th>
                            {Array.from({length: 7}, (_, diaIdx) => {
                                const date = new Date(semana.inicio)
                                date.setDate(date.getDate() + diaIdx)
                                return (
                                    <th key={diaIdx} className={Styles['date-header']}>
                                        {DIAS_SEMANA[diaIdx]}
                                        <br />
                                        {formatarDataDia(date)}
                                    </th>
                                )
                            })}
                        </tr>
                    </thead>
                    <tbody>
                        {ORDEM_LINHAS.map(funcao => {
                            const label = LABEL_FUNCAO[funcao] || funcao
                            const servicosPorDia = semana.servicos[funcao] || Array(7).fill(null)
                            const linha = LINHA_POR_CHAVE[funcao] ?? null
                            return (
                                <tr key={funcao}>
                                    <td className={Styles['label-col']}>{label}</td>
                                    {servicosPorDia.map((servico, diaIdx) => {
                                        const cor = servico ? (semana.coresPorDia[diaIdx] || '#eeeeee') : '#eeeeee'
                                        const destacado = isDestacado(servico, funcao)
                                        const celulaDimmed = queryBusca && !destacado

                                        return (
                                            <td
                                                key={diaIdx}
                                                className={corClassMap[cor] || ''}
                                                style={celulaDimmed ? {opacity: 0.3} : undefined}
                                            >
                                                {servico ? (
                                                    <span
                                                        className={Styles['celula-militar']}
                                                        onClick={() => setModalServico({...servico, funcao, linha})}
                                                    >
                                                        {`${servico.patente || ''} ${servico.nomePaz || ''}`.trim()}
                                                    </span>
                                                ) : (
                                                    <span
                                                        className={Styles['celula-vazia']}
                                                        onClick={() => {
                                                            const date = new Date(semana.inicio)
                                                            date.setDate(date.getDate() + diaIdx)
                                                            const dataStr = date.toISOString().split('T')[0]
                                                            setModalServico({dataServico: dataStr, funcao, linha})
                                                        }}
                                                    >
                                                        ---------------
                                                    </span>
                                                )}
                                            </td>
                                        )
                                    })}
                                </tr>
                            )
                        })}
                    </tbody>
                </table>
            ))}
            <ModalEscala
                abrir={modalServico !== null}
                fechar={() => setModalServico(null)}
                servico={modalServico}
            />
        </>
    )
}
