import Styles from './styles.module.css'
import {useContext, useEffect, useRef, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import UsuarioClient from "../../clients/UsuarioClient.js";

export default function AtualizacaoSenha() {
  const navigate = useNavigate()
  const { setToken, setFeedback } = useContext(GlobalContext)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [carregandoAtualizacao, setCarregandoAtualizacao] = useState(false)

  const abortControllerRef = useRef(null)

  useEffect(() => {
    return () => {
      if (abortControllerRef.current)
        abortControllerRef.current.abort()
    }
  }, [])

  const criarAbortController = () => {
    abortControllerRef.current?.abort()
    const controller = new AbortController()
    abortControllerRef.current = controller
    return controller
  }

  const gerenciarAtualizacaoSenha = evento => {
    evento.preventDefault()

    const form = evento.currentTarget

    if (!form.checkValidity()) {
      for (const element of form.elements) {
        if (element.willValidate && !element.checkValidity()) {
          return setFeedback({ type: 'info', mensagem: element.validationMessage })
        }
      }
    }

    if (!email || !password || !newPassword) {
      setFeedback({ type: 'info', mensagem: 'Preencha email, senha e a nova senha.' })
      return
    }

    const regexPass = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{9,50}$/
    if (!regexPass.test(newPassword)) {
      setFeedback({ type: 'info', mensagem: 'Por favor, digite uma senha válida.' })
      return
    }

    const controller = criarAbortController()

    setCarregandoAtualizacao(true)

    const dadosAtualizacao = { email, password, newPassword }

    UsuarioClient.atualizarPassword(dadosAtualizacao, controller.signal)
      .then(() => {
        setToken("")
        navigate('/login')
        setFeedback({ type: 'success', mensagem: 'Senha atualizada com sucesso.' })
      })
      .catch(error => {
        if (error.name === 'AbortError') return
        setFeedback({ type: 'error', mensagem: error.message })
      })
      .finally(() => {
        setCarregandoAtualizacao(false)
        if (abortControllerRef.current === controller)
          abortControllerRef.current = null
      })
  }

  return (
    <div className={Styles.page}>
      <h1 className={Styles.title}>Escalante</h1>
      <div className={Styles.container}>
        <h2>Atualizar Senha</h2>
        <form className={Styles.form} onSubmit={gerenciarAtualizacaoSenha} noValidate>
          <label className={Styles.label} htmlFor="email">Email</label>
          <input
            className={Styles.input}
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

          <label className={Styles.label} htmlFor="password">Senha</label>
          <input
            className={Styles.input}
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

          <label className={Styles.label} htmlFor="new-password">Nova Senha</label>
          <input
              className={Styles.input}
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

          <button
              type="submit"
              disabled={carregandoAtualizacao}
              className={Styles.button}
          >
            {carregandoAtualizacao ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>

    </div>
  )
}
