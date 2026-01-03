import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext.jsx'
import Modal from '../modal/Modal.jsx'
import BotoesModal from '../modal/BotoesModal.jsx'
import Styles from './styles.module.css'

export default function CadastroMilitar({abrir, fechar}) {
    const {setMilitares, setFeedback} = useContext(GlobalContext)

    const militarModelo = {
        antiguidade: '',
        matricula: '',
        patente: '',
        nomePaz: '',
        nascimento: '',
        folgaEspecial: '',
        cov: false
    }

    const [militar, setMilitar] = useState(militarModelo)

    const converterMaiusculas = nome => nome.toUpperCase()
    const removerEspacosExtras = nome => nome.trim().replace(/\s+/g, ' ')

    const cadastrarMilitar = evento => {
        evento.preventDefault()

        const form = evento.currentTarget
        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        const novoMilitar = {...militar}
        novoMilitar.antiguidade = novoMilitar.antiguidade ? Number(novoMilitar.antiguidade) : undefined
        novoMilitar.folgaEspecial = novoMilitar.folgaEspecial ? Number(novoMilitar.folgaEspecial) : 0

        setMilitares(prev => [...(prev || []), novoMilitar])
        setMilitar(militarModelo)
        fechar()
        setFeedback({type: 'success', mensagem: 'Militar cadastrado com sucesso.'})
    }

    return (
        <Modal abrir={abrir} fechar={fechar} titulo="Adicionar Militar">
            <form onSubmit={cadastrarMilitar} className={Styles.cadastro} noValidate>

                <label>Matrícula:</label>
                <input
                    type="text"
                    placeholder="Ex: 1234567X"
                    value={militar.matricula}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setMilitar({...militar, matricula: e.target.value})
                    }}
                    required
                    pattern="[A-Z0-9]{8,8}"
                    title="A matrícula deve conter exatamente 8 caracteres, sendo apenas letras maiúsculas e números."
                    maxLength="8"
                    minLength="8"
                    onInvalid={e => e.target.setCustomValidity('Por favor, digite uma matrícula válida.')}
                />

                <label>Nome de Paz:</label>
                <input
                    type="text"
                    placeholder="Ex: FULANO DE TAL"
                    value={militar.nomePaz}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setMilitar({...militar, nomePaz: converterMaiusculas(e.target.value)})
                    }}
                    onBlur={e => setMilitar({...militar, nomePaz: removerEspacosExtras(e.target.value)})}
                    required
                    pattern="(?=.*[a-zA-ZáàâãéèêíóôõúçÁÀÂÃÉÈÊÍÓÔÕÚÇ])[a-zA-ZáàâãéèêíóôõúçÁÀÂÃÉÈÊÍÓÔÕÚÇ ]{3,40}"
                    title="O nome do militar deve conter apenas letras e ter entre 3 e 40 caracteres."
                    maxLength="40"
                    minLength="3"
                    onInvalid={e => e.target.setCustomValidity('Por favor, digite um nome válido para o militar.')}
                />

                <label>Posto/Grad.:</label>
                <select
                    name="patente"
                    value={militar.patente}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setMilitar({...militar, patente: e.target.value})
                    }}
                    required
                    onInvalid={e => e.target.setCustomValidity('Por favor, selecione um posto ou graduação.')}
                >
                    <option value="" disabled>Selecione o Posto ou Graduação</option>
                    <option value="TEN">Tenente</option>
                    <option value="SUBTEN">Subtenente</option>
                    <option value="SGT">Sargento</option>
                    <option value="CB">Cabo</option>
                    <option value="SD">Soldado</option>
                </select>

                <label>Antiguidade:</label>
                <input
                    type="number"
                    placeholder="Ex: 1"
                    value={militar.antiguidade}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setMilitar({...militar, antiguidade: e.target.value})
                    }}
                    required
                    min="1"
                    max="999"
                    step="1"
                    title="A antiguidade deve ser um número inteiro positivo."
                    onInvalid={e => e.target.setCustomValidity('Por favor, digite uma antiguidade válida.')}
                />

                <label>Nascimento:</label>
                <input
                    type="date"
                    value={militar.nascimento}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setMilitar({...militar, nascimento: e.target.value})
                    }}
                    required
                    onInvalid={e => e.target.setCustomValidity('Por favor, digite uma data válida.')}
                />

                <label>Folga Especial:</label>
                <input
                    type="number"
                    placeholder="Ex: 3"
                    value={militar.folgaEspecial || 0}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setMilitar({...militar, folgaEspecial: e.target.value})
                    }}
                    required
                    min="0"
                    max="30"
                    step="1"
                    title="A folga especial deve ser um número inteiro positivo ou zero."
                    onInvalid={e => e.target.setCustomValidity('Por favor, digite uma folga válida.')}
                />

                <span className={Styles.cov}>
                    <input
                        type="checkbox"
                        checked={militar.cov === true}
                        onChange={e => setMilitar({...militar, cov: e.target.checked})}
                    />
                    C.O.V.
                </span>

                <BotoesModal
                    typeConfirmar="submit"
                    cancelar={fechar}
                />

            </form>
        </Modal>
    )
}
