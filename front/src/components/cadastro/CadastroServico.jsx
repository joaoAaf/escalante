import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import {ordenarEscala} from '../../utils/ordenacaoEscala'
import {obterProximoId} from '../../utils/geradorIds'
import Modal from '../modal/Modal'
import FormServico from "../form_modal/FormServico.jsx";

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

    return (
        <Modal abrir={abrir} fechar={fechar} titulo="Adicionar Serviço">
            <FormServico servico={servico} setServico={setServico} fechar={fechar} acao={cadastrarServico}/>
        </Modal>
    )
}
