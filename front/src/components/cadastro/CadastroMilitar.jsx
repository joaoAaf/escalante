import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext.jsx'
import Modal from '../modal/Modal.jsx'
import FormMilitar from "../form_modal/FormMilitar.jsx";

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
            <FormMilitar militar={militar} setMilitar={setMilitar} fechar={fechar} acao={cadastrarMilitar}/>
        </Modal>
    )
}
