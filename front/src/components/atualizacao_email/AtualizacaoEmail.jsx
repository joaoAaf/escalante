import {useContext, useRef, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'
import Edit from './assets/edit.png'
import Styles from './styles.module.css'
import UsuarioClient from "../../clients/UsuarioClient.js";
import InputEmail from "../input_email/InputEmail.jsx";

export default function AtualizacaoEmail({email, setEmail}) {

    const {token, setFeedback} = useContext(GlobalContext)
    const [statusModal, setStatusModal] = useState(false)
    const [inputEmail, setInputEmail] = useState(email)
    const [inputConfirmacaoEmail, setInputConfirmacaoEmail] = useState('')

    const abortControllerRef = useRef(null)

    const criarAbortController = () => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller
        return controller
    }

    const atualizarEmail = async () => {

        if (!inputEmail || !inputConfirmacaoEmail) {
            return
        }

        if (inputEmail === email) {
            return setFeedback({type: 'info', mensagem: 'O novo email deve ser diferente do email atual.'})
        }

        if (inputEmail !== inputConfirmacaoEmail) {
            return setFeedback({type: 'info', mensagem: 'Os emails não coincidem. Por favor, verifique.'})
        }

        setStatusModal(false)

        const controller = criarAbortController()

        UsuarioClient.atualizarUsername(token, inputEmail, controller.signal)
            .then(usuario => {
                setEmail(usuario.username)
                setFeedback({type: 'success', mensagem: 'Email atualizado com sucesso.'})
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

    return (
        <>
            <a
                href="#"
                onClick={() => setStatusModal(true)}
                className={Styles.linkAtualizacao}
            >
                <img src={Edit} alt="Atualizar Email"/>
            </a>
            <Modal abrir={statusModal} fechar={() => setStatusModal(false)} titulo="Atualizar Email">
                <div className={Styles.formEmail}>
                    <InputEmail email={inputEmail} setEmail={setInputEmail}/>
                    <InputEmail email={inputConfirmacaoEmail} setEmail={setInputConfirmacaoEmail}
                                label="Confirmação Email:"/>
                    <BotoesModal confirmar={atualizarEmail} cancelar={() => setStatusModal(false)}/>
                </div>
            </Modal>

        </>
    )

}
