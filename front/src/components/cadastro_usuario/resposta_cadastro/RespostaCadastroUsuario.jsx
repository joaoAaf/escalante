import Modal from '../../modal/Modal.jsx'
import Styles from './styles.module.css'

export default function RespostaCadastroUsuario({ abrir, fechar, usuario }) {
  if (!usuario) return null
  const { username, perfis, senhaGerada } = usuario
  return (
    <Modal abrir={abrir} fechar={fechar} titulo="Usuário Cadastrado">
      <button className={Styles.fechar} onClick={fechar} aria-label="Fechar">×</button>
      <p>Usuário: <strong>{username}</strong></p>
      <p>Perfis: <strong>{(perfis || []).join(', ')}</strong></p>
      {senhaGerada ? (
        <div className={Styles.senhaBox}>
          <p>Senha temporária gerada:</p>
          <pre className={Styles.senha}>{senhaGerada}</pre>
          <p className={Styles.obs}>Anote esta senha, ela só poderá ser visualizada agora.</p>
        </div>
      ) : (
        <p>Senha não fornecida pelo servidor.</p>
      )}
    </Modal>
  )
}
