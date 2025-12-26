import {useContext, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import UsuarioClient from "../../clients/UsuarioClient.js";
import PaginaAutenticacao from '../../components/pagina-autenticacao/PaginaAutenticacao'

export default function AtualizacaoSenha() {
  const navigate = useNavigate()
  const { setToken, setFeedback } = useContext(GlobalContext)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')

  const gerenciarAtualizacaoSenha = async (form, controller) => {
    if (!email || !password || !newPassword) {
      return setFeedback({ type: 'info', mensagem: 'Preencha email, senha e a nova senha.' })
    }

    const regexPass = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{9,50}$/
    if (!regexPass.test(newPassword)) {
      return setFeedback({ type: 'info', mensagem: 'Por favor, digite uma senha válida.' })
    }

    const dadosAtualizacao = { email, password, newPassword }

    await UsuarioClient.atualizarPassword(dadosAtualizacao, controller.signal)
    setToken("")
    navigate('/login')
    setFeedback({ type: 'success', mensagem: 'Senha atualizada com sucesso.' })
  }

  return (
    <PaginaAutenticacao onSubmit={gerenciarAtualizacaoSenha} submitButton="Atualizar" submitButtonDisabled="Atualizando...">
      <h2>Atualizar Senha</h2>

      <label htmlFor="email">Email</label>
      <input
         type="email"
         value={email}
         onChange={(e) => {
           e.target.setCustomValidity("")
           setEmail(e.target.value)
         }}
         placeholder="email@exemplo.com"
         required
         maxLength="130"
        onInvalid={e => e.target.setCustomValidity("Por favor, digite um email válido.")}
      />

      <label htmlFor="password">Senha</label>
      <input
         type="password"
         value={password}
         onChange={(e) => {
           e.target.setCustomValidity("")
           setPassword(e.target.value)
         }}
         placeholder="********"
         required
         maxLength="50"
        onInvalid={e => e.target.setCustomValidity("Por favor, digite sua senha.")}
      />

      <label htmlFor="new-password">Nova Senha</label>
      <input
           type="password"
           value={newPassword}
           onChange={(e) => {
             e.target.setCustomValidity("")
             setNewPassword(e.target.value)
           }}
           placeholder="********"
           required
           minLength="9"
           maxLength="50"
           title="A senha deve conter entre 9 e 50 caracteres, pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial."
          onInvalid={e => e.target.setCustomValidity("Por favor, digite uma senha válida.")}
       />

    </PaginaAutenticacao>
  )
}
