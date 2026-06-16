import Styles from './styles.module.css'
import BotoesModal from "../modal/BotoesModal.jsx";
import GlobalContext from "../../context/GlobalContext.jsx";
import {useContext, useRef} from "react";
import MilitarClient from "../../clients/MilitarClient.js";

const FUNCOES = [
    'Fiscal de Dia',
    'C.O.V.',
    'Operador de Linha 1',
    'Ajudante de Linha',
    'Operador de Linha 2',
    'Permanente'
]

const FUNCAO_OPERADOR_LINHA_1 = 'Operador de Linha 1'
const FUNCAO_OPERADOR_LINHA_2 = 'Operador de Linha 2'

export default function FormServico({servico, setServico, fechar, acao}) {

    const {token, setFeedback} = useContext(GlobalContext)

    const abortControllerRef = useRef(null)

    const criarAbortController = () => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller
        return controller
    }

    const mapPatentes = {'TEN': 'Tenente', 'SUBTEN': 'Subtenente', 'SGT': 'Sargento', 'CB': 'Cabo', 'SD': 'Soldado'}

    const dadosMilitar = async matricula => {
        if (!matricula || matricula.length !== 8) return
        const controller = criarAbortController()
        MilitarClient.obterMilitarPorMatricula(matricula, token, controller.signal)
            .then(militar => {
                setServico(prev => ({
                    ...prev,
                    nomePaz: militar.nomePaz,
                    patente: mapPatentes[militar.patente]
                }))
            })
            .catch(error => {
                if (error.name === 'AbortError') return
                setFeedback({type: 'error', mensagem: error.message})
            })
            .finally(() => {
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }

    const converterMaiusculas = nome => nome.toUpperCase()
    const removerEspacosExtras = nome => nome.trim().replace(/\s+/g, ' ')

    const isOperadorLinha = servico.funcao === FUNCAO_OPERADOR_LINHA_1 || servico.funcao === FUNCAO_OPERADOR_LINHA_2

    return (
        <form onSubmit={acao} className={Styles.form} noValidate>

            <label>Data do Serviço:</label>
            <input
                type="date"
                value={servico.dataServico}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, dataServico: e.target.value}))
                }}
                required
                onInvalid={e => e.target.setCustomValidity("Por favor, digite uma data válida.")}
            />

            <label>Matrícula:</label>
            <input
                type="text"
                placeholder="Ex: 1234567X"
                value={servico.matricula}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, matricula: converterMaiusculas(e.target.value)}))
                }}
                onBlur={e => dadosMilitar(e.target.value)}
                required
                pattern="[A-Z0-9]{8,8}"
                title="A matrícula deve conter exatamente 8 caracteres, sendo apenas letras maiúsculas e números."
                maxLength="8"
                minLength="8"
                onInvalid={e => e.target.setCustomValidity("Por favor, digite uma matrícula válida.")}
            />

            <label>Militar Escalado:</label>
            <input
                type="text"
                placeholder="Ex: FULANO DE TAL"
                value={servico.nomePaz}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, nomePaz: converterMaiusculas(e.target.value)}))
                }}
                onBlur={e => setServico(prev => ({...prev, nomePaz: removerEspacosExtras(e.target.value)}))}
                required
                pattern="(?=.*[a-zA-ZáàâãéèêíóôõúçÁÀÂÃÉÈÊÍÓÔÕÚÇ])[a-zA-ZáàâãéèêíóôõúçÁÀÂÃÉÈÊÍÓÔÕÚÇ ]{3,20}"
                title="O nome do militar deve conter apenas letras e ter entre 3 e 20 caracteres."
                maxLength="20"
                minLength="3"
                onInvalid={e => e.target.setCustomValidity("Por favor, digite um nome válido para o militar.")}
            />

            <label>Posto/Grad.:</label>
            <select
                name="patente"
                value={servico.patente}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, patente: e.target.value}))
                }}
                required
                onInvalid={e => e.target.setCustomValidity("Por favor, selecione um posto ou graduação.")}
            >
                <option value="" disabled>Selecione o Posto ou Graduação</option>
                <option>Tenente</option>
                <option>Subtenente</option>
                <option>Sargento</option>
                <option>Cabo</option>
                <option>Soldado</option>
            </select>

            <label>Função:</label>
            <select
                name="funcao"
                value={servico.funcao}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => {
                        const novaFuncao = e.target.value
                        const isOperador = novaFuncao === FUNCAO_OPERADOR_LINHA_1 || novaFuncao === FUNCAO_OPERADOR_LINHA_2
                        return {
                            ...prev,
                            funcao: novaFuncao,
                            linha: isOperador ? (prev.linha || 1) : null
                        }
                    })
                }}
                required
                onInvalid={e => e.target.setCustomValidity("Por favor, selecione uma função.")}
            >
                <option value="" disabled>Selecione a Função</option>
                {FUNCOES.map(f => (
                    <option key={f} value={f}>{f}</option>
                ))}
            </select>

            {isOperadorLinha && (
                <>
                    <label>Linha:</label>
                    <select
                        name="linha"
                        value={servico.linha ?? 1}
                        onChange={e => {
                            e.target.setCustomValidity("")
                            setServico(prev => ({...prev, linha: Number(e.target.value)}))
                        }}
                    >
                        <option value={1}>Linha 1</option>
                        <option value={2}>Linha 2</option>
                    </select>
                </>
            )}

            <BotoesModal typeConfirmar="submit" cancelar={fechar}/>
        </form>
    )
}
