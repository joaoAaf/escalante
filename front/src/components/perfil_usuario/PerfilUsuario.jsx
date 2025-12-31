import Modal from '../modal/Modal.jsx'
import Styles from './styles.module.css'

export default function PerfilUsuario({abrir, fechar, usuario, onAlterarSenha}) {
    if (!usuario) return null
    const { email, perfis } = usuario
    return (
        <Modal abrir={abrir} fechar={fechar} titulo="Meu Perfil">
            <button className={Styles.fechar} onClick={fechar} aria-label="Fechar">×</button>
            <p>Email: <strong>{email}</strong></p>
            <p>Perfis: <strong>{(perfis || []).join(', ')}</strong></p>
            <div className={Styles.acoes}>
                <button className={Styles.alterarSenha} onClick={() => onAlterarSenha()}>Alterar senha</button>
            </div>
        </Modal>
    )
}

