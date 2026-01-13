import Styles from './styles.module.css'
import BotoesModal from "../modal/BotoesModal.jsx";
import GlobalContext from "../../context/GlobalContext.jsx";
import {useContext, useRef} from "react";
import MilitarClient from "../../clients/MilitarClient.js";

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
                    patente: mapPatentes[militar.patente],
                    antiguidade: militar.antiguidade,
                    folga: militar.folgaEspecial !== 0 ? militar.folgaEspecial : ''
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

            <label>Antiguidade:</label>
            <input
                type="number"
                placeholder="Ex: 1"
                value={servico.antiguidade}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, antiguidade: e.target.value}))
                }}
                required
                min="1"
                max="999"
                step="1"
                title="A antiguidade deve ser um número inteiro positivo."
                onInvalid={e => e.target.setCustomValidity("Por favor, digite uma antiguidade válida.")}
            />

            <label>Função:</label>
            <select
                name="funcao"
                value={servico.funcao}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, funcao: e.target.value}))
                }}
                required
                onInvalid={e => e.target.setCustomValidity("Por favor, selecione uma função.")}
            >
                <option value="" disabled>Selecione a Função</option>
                <option>Fiscal de Dia</option>
                <option>C.O.V.</option>
                <option>Operador de Linha</option>
                <option>Ajudante de Linha</option>
                <option>Permanente</option>
            </select>

            <label>Folga:</label>
            <input
                type="number"
                placeholder="Ex: 3"
                value={servico.folga}
                onChange={e => {
                    e.target.setCustomValidity("")
                    setServico(prev => ({...prev, folga: e.target.value}))
                }}
                required
                min="1"
                max="30"
                step="1"
                title="A folga deve ser um número inteiro positivo."
                onInvalid={e => e.target.setCustomValidity("Por favor, digite uma folga válida.")}
            />

            <BotoesModal typeConfirmar="submit" cancelar={fechar}/>
        </form>
    )
}