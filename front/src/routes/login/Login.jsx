import Styles from './styles.module.css'
import {useContext, useEffect, useRef, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import LoginClient from "../../clients/LoginClient";

export default function Login() {
  const navigate = useNavigate()
  const { setToken, setFeedback } = useContext(GlobalContext)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [carregandoLogin, setCarregandoLogin] = useState(false)

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

  const gerenciarLogin = evento => {
    evento.preventDefault()

    const form = evento.currentTarget

    if (!form.checkValidity()) {
      for (const element of form.elements) {
        if (element.willValidate && !element.checkValidity()) {
          return setFeedback({ type: 'info', mensagem: element.validationMessage })
        }
      }
    }

    if (!email || !password) {
      setFeedback({ type: 'info', mensagem: 'Preencha email e senha.' })
      return
    }

    const controller = criarAbortController()

    setCarregandoLogin(true)

    const dadosLogin = { email, password }

    LoginClient.login(dadosLogin, controller.signal)
      .then(token => {
        setToken(token)
        navigate('/')
        setFeedback({ type: 'success', mensagem: 'Login realizado com sucesso.' })
      })
      .catch(error => {
        if (error.name === 'AbortError') return
        setFeedback({ type: 'error', mensagem: error.message })
      })
      .finally(() => {
        setCarregandoLogin(false)
        if (abortControllerRef.current === controller)
          abortControllerRef.current = null
      })
  }

  return (
    <div className={Styles.page}>
      <h1 className={Styles.title}>Escalante</h1>
      <div className={Styles.container}>
        <h2>Entrar</h2>
        <form className={Styles.form} onSubmit={gerenciarLogin} noValidate>
          <label className={Styles.label} htmlFor="email">Email</label>
          <input
            id="email"
            className={Styles.input}
            type="email"
            value={email}
            onChange={(e) => {
              e.target.setCustomValidity("")
              setEmail(e.target.value)
            }}
            placeholder="seu@exemplo.com"
            required
            onInvalid={e => e.target.setCustomValidity("Por favor, digite um email válido.")}
          />

          <label className={Styles.label} htmlFor="password">Senha</label>
          <input
            id="password"
            className={Styles.input}
            type="password"
            value={password}
            onChange={(e) => {
              e.target.setCustomValidity("")
              setPassword(e.target.value)
            }}
            placeholder="********"
            required
            onInvalid={e => e.target.setCustomValidity("Por favor, digite sua senha.")}
          />

          <button
              type="submit"
              disabled={carregandoLogin}
              className={Styles.button}
          >
            {carregandoLogin ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </div>

    </div>
  )
}
