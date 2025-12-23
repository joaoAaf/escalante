import Styles from './styles.module.css'
import {useContext, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'

export default function Login() {
  const navigate = useNavigate()
  const { setFeedback } = useContext(GlobalContext)

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!email || !password) {
      setFeedback({ tipo: 'erro', mensagem: 'Preencha email e senha.' })
      return
    }

    setFeedback({ tipo: 'sucesso', mensagem: 'Login realizado com sucesso (simulado).' })
    navigate('/')
  }

  return (
    <div className={Styles.page}>
      <h1 className={Styles.title}>Escalante</h1>
      <div className={Styles.container}>
        <h2>Entrar</h2>
        <form className={Styles.form} onSubmit={handleSubmit}>
          <label className={Styles.label} htmlFor="email">Email</label>
          <input
            id="email"
            className={Styles.input}
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="seu@exemplo.com"
          />

          <label className={Styles.label} htmlFor="password">Senha</label>
          <input
            id="password"
            className={Styles.input}
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="********"
          />

          <button type="submit" className={Styles.button}>Entrar</button>
        </form>
      </div>
    </div>
  )
}
