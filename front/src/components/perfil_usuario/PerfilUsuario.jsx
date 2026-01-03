import Modal from '../modal/Modal.jsx'
import Styles from './styles.module.css'
import {useContext, useState} from "react";
import {useNavigate} from 'react-router-dom'
import GlobalContext from '../../context/GlobalContext'
import AtualizacaoEmail from "../atualizacao_email/AtualizacaoEmail.jsx";

export default function PerfilUsuario({abrir, fechar, usuario, alterarSenha}) {

    const {username, perfis} = usuario || {username: "", perfis: []}

    const [email, setEmail] = useState(username)
    const {setToken} = useContext(GlobalContext)
    const navigate = useNavigate()

    if (!username || !perfis || perfis.length === 0) return

    const logout = () => {
        setToken("")
        fechar()
        navigate('/login')
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
                <button id={Styles.sair} className={Styles.botoes} onClick={logout}>Sair</button>
            </div>
        </Modal>
    )
}
