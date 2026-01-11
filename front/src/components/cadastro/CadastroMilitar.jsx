import {useContext, useRef, useState} from 'react'
import GlobalContext from '../../context/GlobalContext.jsx'
import Modal from '../modal/Modal.jsx'
import FormMilitar from "../form_modal/FormMilitar.jsx";
import MilitarClient from "../../clients/MilitarClient.js";

export default function CadastroMilitar({abrir, fechar}) {
    const {token, setReload, setFeedback} = useContext(GlobalContext)

    const militarModelo = {
        antiguidade: 0,
        matricula: '',
        patente: '',
        nomePaz: '',
        nascimento: '',
        folgaEspecial: 0,
        cov: false
    }

    const [militar, setMilitar] = useState(militarModelo)
    const [salvando, setSalvando] = useState(false)

    const abortControllerRef = useRef(null)

    const criarAbortController = () => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller
        return controller
    }

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

        if (!militar?.antiguidade) setMilitar({...militar, antiguidade: 0})
        if (!militar?.folgaEspecial) setMilitar({...militar, folgaEspecial: 0})

        const controller = criarAbortController()
        setSalvando(true)
        MilitarClient.cadastrarMilitares([militar], token, controller.signal)
            .then(() => {
                setReload(true)
                fechar()
                setFeedback({type: 'success', mensagem: 'Militar cadastrado com sucesso.'})
            })
            .catch(error => {
                if (error.name === 'AbortError') return
                setFeedback({type: 'error', mensagem: error.message})
            })
            .finally(() => {
                setSalvando(false)
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }

    return (
        <Modal abrir={abrir} fechar={fechar} titulo="Adicionar Militar">
            <FormMilitar militar={militar} setMilitar={setMilitar} fechar={fechar} acao={cadastrarMilitar}
                         salvando={salvando}/>
        </Modal>
    )
}
