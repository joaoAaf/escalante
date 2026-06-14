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

function normalizarFuncao(f) {
    return f === 'Operador de Linha' ? 'Operador de Linha 1' : (f || '')
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
            setFuncao(normalizarFuncao(servico.funcao))
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
            setEscala(escala => ordenarEscala([...escala, {
                id: novoId,
                dataServico: data,
                funcao,
                matricula: militar.matricula,
                nomePaz: militar.nomePaz,
                patente: militar.patente
            }]))
            fechar()
        } else {
            if (!servico?.id) return
            const militar = militares.find(m => m.matricula === militarMatricula)
            const novaData = data || servico.dataServico
            const novaFuncao = funcao || servico.funcao

            setEscala(escala => {
                const conflito = escala.find(s =>
                    s.dataServico === novaData &&
                    s.funcao === novaFuncao &&
                    s.id !== servico.id
                )

                const updated = escala.map(s => {
                    if (s.id === servico.id) {
                        return {
                            ...s,
                            dataServico: novaData,
                            funcao: novaFuncao,
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
                            funcao: servico.funcao
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
                />
                {!isNovo && (
                    <button
                        onClick={handleRemover}
                        style={{
                            padding: '8px 20px',
                            backgroundColor: '#ef5c41',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontWeight: 'bold',
                            alignSelf: 'flex-start'
                        }}
                    >
                        Remover Serviço
                    </button>
                )}
            </div>
        </Modal>
    )
}
