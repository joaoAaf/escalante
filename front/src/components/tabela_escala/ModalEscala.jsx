import {useState, useContext, useEffect} from 'react'
import GlobalContext from '../../context/GlobalContext'
import {ordenarEscala} from '../../utils/ordenacaoEscala'
import MilitarClient from '../../clients/MilitarClient'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'

const FUNCOES = [
    'Fiscal de Dia',
    'C.O.V.',
    'Operador de Linha 1',
    'Ajudante de Linha',
    'Operador de Linha 2',
    'Permanente'
]

const FUNCAO_OPERADOR_LINHA = 'Operador de Linha'

function normalizarParaExibicao(f, l) {
    if (f === FUNCAO_OPERADOR_LINHA)
        return l === 2 ? 'Operador de Linha 2' : 'Operador de Linha 1'
    return f || ''
}

function normalizarParaPersistencia(funcao) {
    if (funcao === 'Operador de Linha 1' || funcao === 'Operador de Linha 2')
        return {funcao: FUNCAO_OPERADOR_LINHA, linha: funcao === 'Operador de Linha 2' ? 2 : 1}
    return {funcao, linha: null}
}

export default function ModalEscala({abrir, fechar, servico}) {
    const {militares, setMilitares, setEscala, token} = useContext(GlobalContext)
    const [data, setData] = useState('')
    const [funcao, setFuncao] = useState('')
    const [militarMatricula, setMilitarMatricula] = useState('')

    const isNovo = servico && !servico.id

    useEffect(() => {
        if (abrir && (!militares || militares.length === 0) && token) {
            MilitarClient.listarMilitares(token)
                .then(lista => {
                    const ordenada = (lista || []).sort((a, b) => a.antiguidade - b.antiguidade)
                    setMilitares(ordenada)
                })
                .catch(() => {})
        }
    }, [abrir, token, militares, setMilitares])

    useEffect(() => {
        if (abrir && servico) {
            setData(servico.dataServico || '')
            setFuncao(normalizarParaExibicao(servico.funcao, servico.linha))
            if (isNovo) {
                setMilitarMatricula('')
            } else {
                const militar = militares?.find(m => m.matricula === servico.matricula)
                setMilitarMatricula(militar?.matricula ?? '')
            }
        }
    }, [abrir, servico, isNovo, militares])

    const handleSalvar = () => {
        if (isNovo) {
            if (!militarMatricula || !data || !funcao) return
            const militar = militares.find(m => m.matricula === militarMatricula)
            if (!militar) return
            const novoId = Date.now()
            const {funcao: funcaoNormalizada, linha: linhaNormalizada} = normalizarParaPersistencia(funcao)
            setEscala(escala => ordenarEscala([...escala, {
                id: novoId,
                dataServico: data,
                funcao: funcaoNormalizada,
                linha: linhaNormalizada,
                matricula: militar.matricula,
                nomePaz: militar.nomePaz,
                patente: militar.patente
            }]))
            fechar()
        } else {
            if (!servico?.id) return
            const militar = militares.find(m => m.matricula === militarMatricula)
            const {funcao: novaFuncao, linha: novaLinha} = normalizarParaPersistencia(funcao)
            const novaData = data || servico.dataServico

            setEscala(escala => {
                const conflito = escala.find(s =>
                    s.dataServico === novaData &&
                    s.funcao === novaFuncao &&
                    (novaFuncao !== FUNCAO_OPERADOR_LINHA || s.linha === novaLinha) &&
                    s.id !== servico.id
                )

                const updated = escala.map(s => {
                    if (s.id === servico.id) {
                        return {
                            ...s,
                            dataServico: novaData,
                            funcao: novaFuncao,
                            linha: novaLinha,
                            ...(militar ? {
                                matricula: militar.matricula,
                                nomePaz: militar.nomePaz,
                                patente: militar.patente
                            } : {})
                        }
                    }
                    if (conflito && s.id === conflito.id) {
                        return {
                            ...s,
                            dataServico: servico.dataServico,
                            funcao: servico.funcao,
                            linha: servico.linha ?? null
                        }
                    }
                    return s
                })
                return ordenarEscala(updated)
            })
            fechar()
        }
    }

    const handleRemover = () => {
        if (!servico?.id) return
        setEscala(escala => escala.filter(s => s.id !== servico.id))
        fechar()
    }

    return (
        <Modal abrir={abrir} fechar={fechar} titulo={isNovo ? 'Novo Serviço' : 'Editar Serviço'}>
            <div style={{display: 'flex', flexDirection: 'column', gap: '16px'}}>
                <div>
                    <label style={{display: 'block', marginBottom: '4px', fontWeight: 'bold'}}>Militar:</label>
                    <select
                        value={militarMatricula}
                        onChange={e => setMilitarMatricula(e.target.value)}
                        style={{width: '100%', padding: '8px', border: '1px solid #ccc', borderRadius: '4px'}}
                    >
                        <option value="">Selecione um militar...</option>
                        {militares?.map(m => (
                            <option key={m.matricula} value={m.matricula}>
                                {`${m.patente || ''} ${m.nomePaz || ''} - ${m.matricula || ''}`.trim()}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label style={{display: 'block', marginBottom: '4px', fontWeight: 'bold'}}>Data:</label>
                    <input
                        type="date"
                        value={data}
                        onChange={e => setData(e.target.value)}
                        style={{width: '100%', padding: '8px', border: '1px solid #ccc', borderRadius: '4px'}}
                    />
                </div>
                <div>
                    <label style={{display: 'block', marginBottom: '4px', fontWeight: 'bold'}}>Função:</label>
                    <select
                        value={funcao}
                        onChange={e => setFuncao(e.target.value)}
                        style={{width: '100%', padding: '8px', border: '1px solid #ccc', borderRadius: '4px'}}
                    >
                        {FUNCOES.map(f => (
                            <option key={f} value={f}>{f}</option>
                        ))}
                    </select>
                </div>
                <BotoesModal
                    typeConfirmar="button"
                    confirmar={handleSalvar}
                    cancelar={fechar}
                    textoConfirmar="Salvar"
                    isNovo={!isNovo}
                    handleRemover={handleRemover}
                />
            </div>
        </Modal>
    )
}
