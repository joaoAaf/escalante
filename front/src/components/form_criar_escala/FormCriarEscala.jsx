import {useContext, useEffect, useRef, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import Styles from './styles.module.css'
import EscalaClient from '../../clients/EscalaClient'
import {inserirIds} from '../../utils/geradorIds'

export default function FormCriarEscala({servicosAnteriores}) {

    const {token, militares, setEscala, setFeedback} = useContext(GlobalContext)

    const [dataInicio, setDataInicio] = useState('')
    const [dataFim, setDataFim] = useState('')
    const [diasServico, setDiasServico] = useState(2)
    const [escalaVazia, setEscalaVazia] = useState(false)
    const [carregandoEscala, setCarregandoEscala] = useState(false)

    const abortControllerRef = useRef(null)

    useEffect(() => {
        return () => {
            if (abortControllerRef.current)
                abortControllerRef.current.abort()
        }
    }, [])

    function normalizarLinhasOperadorLinha(escala) {
        const contadores = new Map()
        return (escala || []).map(servico => {
            if (servico?.funcao !== 'Operador de Linha')
                return {...servico, linha: servico?.linha ?? null}
            const chave = servico.dataServico
            const proximaLinha = (contadores.get(chave) || 0) + 1
            contadores.set(chave, proximaLinha)
            return {...servico, linha: servico.linha ?? proximaLinha}
        })
    }

    function gerarEscalaVazia(dataInicio, dataFim) {
        const funcoes = [
            { funcao: 'Fiscal de Dia', linha: null },
            { funcao: 'C.O.V.', linha: null },
            { funcao: 'Operador de Linha', linha: 1 },
            { funcao: 'Ajudante de Linha', linha: null },
            { funcao: 'Operador de Linha', linha: 2 },
            { funcao: 'Permanente', linha: null }
        ]

        const escala = []
        const inicio = new Date(dataInicio + 'T00:00:00')
        const fim = new Date(dataFim + 'T00:00:00')

        for (let data = new Date(inicio); data <= fim; data.setDate(data.getDate() + 1)) {
            const dataServico = data.toISOString().split('T')[0]

            for (const { funcao, linha } of funcoes) {
                escala.push({
                    dataServico,
                    funcao,
                    linha,
                    matricula: '',
                    nomePaz: '',
                    patente: ''
                })
            }
        }

        return escala
    }

    const criarAbortController = () => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller
        return controller
    }

    const gerenciarCriacaoEscala = evento => {
        evento.preventDefault()

        const form = evento.currentTarget

        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        try {
            const inicio = new Date(dataInicio)
            const fim = new Date(dataFim)

            if (fim <= inicio)
                return setFeedback({type: 'info', mensagem: 'A data final não pode ser anterior à data inicial.'})

            const diffTime = fim - inicio
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

            if (diffDays > 35) {
                return setFeedback({
                    type: 'info',
                    mensagem: 'O intervalo entre as datas não pode ser maior que 35 dias.'
                })
            }

        } catch {
            return setFeedback({type: 'info', mensagem: 'Por favor, verifique as datas informadas.'})
        }

        if (escalaVazia) {
            const escalaVaziaArray = gerarEscalaVazia(dataInicio, dataFim)
            setEscala(escalaVaziaArray)
            setFeedback({type: 'success', mensagem: 'Escala vazia criada com sucesso.'})
            return
        }

        const militaresEscalaveis = (militares || []).filter(m => m.escalavel !== false)

        if (militaresEscalaveis.length === 0)
            return setFeedback({type: 'info', mensagem: 'Não é possível criar a escala sem militares escaláveis.'})

        if (militaresEscalaveis.length > 100)
            return setFeedback({type: 'info', mensagem: 'O número de militares não pode ser maior que 100.'})

        if (servicosAnteriores?.length > 210)
            return setFeedback({type: 'info', mensagem: 'A escala anterior não pode conter mais que 210 serviços.'})

        const controller = criarAbortController()

        setCarregandoEscala(true)

        const dadosEscala = {
            dataInicio,
            dataFim,
            diasServico,
            militares: militaresEscalaveis,
            servicosAnteriores: servicosAnteriores || []
        }

        EscalaClient.criarEscalaAutomatica(dadosEscala, token, controller.signal)
            .then(escala => {
                const escalaResponse = (escala || [])
                const escalaNormalizada = normalizarLinhasOperadorLinha(escalaResponse)
                const escalaComIds = inserirIds(escalaNormalizada)
                setEscala(escalaComIds)
                setFeedback({type: 'success', mensagem: 'Escala criada com sucesso.'})
            })
            .catch(error => {

                if (error.name === 'AbortError') return
                setFeedback({type: 'error', mensagem: error.message})
            })
            .finally(() => {
                setCarregandoEscala(false)
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }

    return (
        <div className={Styles.criar_escala}>
            <h3>Dados para Criação da Escala</h3>
            {servicosAnteriores?.length > 0 && (
                <div className={Styles.mensagem_sucesso}>
                    ✓ Escala anterior carregada: {servicosAnteriores.length} serviços importados.
                </div>
            )}
            <form onSubmit={gerenciarCriacaoEscala} noValidate>
                <div>
                    <label>Data Inicial:</label>
                    <input
                        type="date"
                        value={dataInicio}
                        onChange={e => {
                            e.target.setCustomValidity("")
                            setDataInicio(e.target.value)
                        }}
                        required
                        onInvalid={e => e.target.setCustomValidity("Por favor, digite uma data inicial válida.")}
                    />
                </div>

                <div>
                    <label>Data Final:</label>
                    <input
                        type="date"
                        value={dataFim}
                        onChange={e => {
                            e.target.setCustomValidity("")
                            setDataFim(e.target.value)
                        }}
                        required
                        onInvalid={e => e.target.setCustomValidity("Por favor, digite uma data final válida.")}
                    />
                </div>

                <div>
                    <label>Dias de Serviço:</label>
                    <input
                        type="number"
                        value={diasServico}
                        onChange={e => {
                            e.target.setCustomValidity("")
                            setDiasServico(e.target.value)
                        }}
                        disabled={escalaVazia}
                        required
                        min="1"
                        max="12"
                        step="1"
                        title="Os dias de serviço devem ser um número inteiro positivo."
                        onInvalid={e => e.target.setCustomValidity("Por favor, digite uma quantidade válida de dias de serviço.")}
                    />
                </div>

                <div>
                    <label>
                        <input
                            type="checkbox"
                            checked={escalaVazia}
                            onChange={e => setEscalaVazia(e.target.checked)}
                        />
                        Escala Vazia
                    </label>
                </div>

                <div>
                    <button
                        type="submit"
                        disabled={carregandoEscala}
                    >
                        {carregandoEscala ? "Criando..." : "Criar Escala"}
                    </button>
                </div>
            </form>
        </div>
    )
}
