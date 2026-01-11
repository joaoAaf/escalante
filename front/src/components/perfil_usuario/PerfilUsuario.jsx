import Modal from '../modal/Modal.jsx'
import Styles from './styles.module.css'
import {useContext, useRef, useState} from "react";
import {useNavigate} from 'react-router-dom'
import GlobalContext from '../../context/GlobalContext'
import AtualizacaoEmail from "../atualizacao_pontual/AtualizacaoEmail.jsx";
import LogoutClient from "../../clients/LogoutClient.js";

export default function PerfilUsuario({abrir, fechar, usuario, alterarSenha}) {

    const {username, perfis} = usuario || {username: "", perfis: []}

    const [email, setEmail] = useState(username)
    const [saindo, setSaindo] = useState(false)
    const {token, setToken, setFeedback} = useContext(GlobalContext)
    const navigate = useNavigate()
    const abortControllerRef = useRef(null)

    const criarAbortController = () => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller
        return controller
    }

    if (!username || !perfis || perfis.length === 0) return

    const logout = () => {
        const controller = criarAbortController()
        setSaindo(true)
        LogoutClient.logout(token, controller.signal)
            .then(() => setFeedback({type: 'success', mensagem: 'Logout realizado com sucesso.'}))
            .catch(error => {
                if (error.name === 'AbortError') return
                setFeedback({type: 'error', mensagem: error.message})
            })
            .finally(() => {
                setToken("")
                setSaindo(false)
                fechar()
                navigate('/login')
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }

    return (
        <Modal abrir={abrir} fechar={fechar} titulo="Meu Perfil">
            <button className={Styles.fechar} onClick={fechar} aria-label="Fechar">×</button>
            <span className={Styles.email}>
                <p>Email: <strong>{email}</strong></p>
                <AtualizacaoEmail email={email} setEmail={setEmail}/>
            </span>
            <p>Perfis: <strong>{(perfis || []).join(', ')}</strong></p>
            <div className={Styles.acoes}>
                <button id={Styles.alterar} className={Styles.botoes} onClick={() => alterarSenha()}>
                    Alterar senha
                </button>
                <button
                    id={Styles.sair}
                    className={Styles.botoes}
                    onClick={logout}
                    disabled={saindo}
                >
                    {saindo ? "Saindo..." : "Sair"}
                </button>
            </div>
        </Modal>
    )
}
