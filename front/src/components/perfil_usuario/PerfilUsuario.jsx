import Modal from '../modal/Modal.jsx'
import Styles from './styles.module.css'
import {useState} from "react";
import AtualizacaoEmail from "../atualizacao_email/AtualizacaoEmail.jsx";

export default function PerfilUsuario({abrir, fechar, usuario, alterarSenha}) {

    const {username, perfis} = usuario || {username: "", perfis: []}

    const [email, setEmail] = useState(username)

    if (!username || !perfis || perfis.length === 0) return

    return (
        <Modal abrir={abrir} fechar={fechar} titulo="Meu Perfil">
            <button className={Styles.fechar} onClick={fechar} aria-label="Fechar">×</button>
            <span className={Styles.email}>
                <p>Email: <strong>{email}</strong></p>
                <AtualizacaoEmail email={email} setEmail={setEmail}/>
            </span>
            <p>Perfis: <strong>{(perfis || []).join(', ')}</strong></p>
            <div className={Styles.acoes}>
                <button className={Styles.alterarSenha} onClick={() => alterarSenha()}>Alterar senha</button>
            </div>
        </Modal>
    )
}

