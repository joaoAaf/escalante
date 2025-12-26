// styles are managed by PaginaAutenticacao
import {useContext, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import LoginClient from "../../clients/LoginClient.js";
import PaginaAutenticacao from '../../components/pagina-autenticacao/PaginaAutenticacao'

export default function Login() {
  const navigate = useNavigate()
  const { setToken, setFeedback } = useContext(GlobalContext)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const gerenciarLogin = async (form, controller) => {

    if (!email || !password) {
      return setFeedback({ type: 'info', mensagem: 'Preencha email e senha.' })
    }

    const dadosLogin = { email, password }

    const token = await LoginClient.login(dadosLogin, controller.signal)
    setToken(token)
    navigate('/')
    setFeedback({ type: 'success', mensagem: 'Login realizado com sucesso.' })
  }

  return (
    <PaginaAutenticacao onSubmit={gerenciarLogin} submitButton="Entrar" submitButtonDisabled="Entrando...">
      <h2>Entrar</h2>

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

    </PaginaAutenticacao>
  )
}
