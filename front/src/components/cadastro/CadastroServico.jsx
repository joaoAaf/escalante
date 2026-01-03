import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import Styles from './styles.module.css'
import {ordenarEscala} from '../../utils/ordenacaoEscala'
import {obterProximoId} from '../../utils/geradorIds'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'

export default function CadastroServico({abrir, fechar}) {

    const {escala, setEscala, setFeedback} = useContext(GlobalContext)

    const servicoModelo = {
        dataServico: '',
        matricula: '',
        nomePaz: '',
        patente: '',
        antiguidade: '',
        funcao: '',
        folga: ''
    }

    const [servico, setServico] = useState(servicoModelo)

    const cadastrarServico = evento => {
        evento.preventDefault()

        const form = evento.currentTarget

        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        const servicoComId = {...servico, id: obterProximoId(escala)}
        let novaEscala = [...(escala || []), servicoComId]
        novaEscala = ordenarEscala(novaEscala)
        setEscala(novaEscala)
        setServico(servicoModelo)
        fechar()
    }

    const converterMaiusculas = nome => {
        return nome.toUpperCase()
    }

    const removerEspacosExtras = nome => {
        return nome.trim().replace(/\s+/g, ' ')
    }

    return (
        <>
            <Modal abrir={abrir} fechar={fechar} titulo="Adicionar Serviço">
                <form onSubmit={cadastrarServico} className={Styles.cadastro} noValidate>

                    <label>Data do Serviço:</label>
                    <input
                        type="date"
                        value={servico.dataServico}
                        onChange={e => {
                            e.target.setCustomValidity("")
                            setServico({...servico, dataServico: e.target.value})
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
                            setServico({...servico, matricula: e.target.value})
                        }}
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
                            setServico({...servico, nomePaz: converterMaiusculas(e.target.value)})
                        }}
                        onBlur={e => setServico({...servico, nomePaz: removerEspacosExtras(e.target.value)})}
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
                            setServico({...servico, patente: e.target.value})
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
                            setServico({...servico, antiguidade: e.target.value})
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
                            setServico({...servico, funcao: e.target.value})
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
                            setServico({...servico, folga: e.target.value})
                        }}
                        required
                        min="1"
                        max="30"
                        step="1"
                        title="A folga deve ser um número inteiro positivo."
                        onInvalid={e => e.target.setCustomValidity("Por favor, digite uma folga válida.")}
                    />

                    <BotoesModal
                        typeConfirmar="submit"
                        cancelar={fechar}
                    />

                </form>
            </Modal>
        </>
    )
}
