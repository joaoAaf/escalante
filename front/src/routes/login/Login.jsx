import {useContext, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import LoginClient from "../../clients/LoginClient.js";
import PaginaAutenticacao from '../../components/pagina_autenticacao/PaginaAutenticacao'
import InputEmail from "../../components/input_email/InputEmail.jsx";
import InputSenha from "../../components/input_senha/InputSenha.jsx";
import Styles from './styles.module.css'

export default function Login() {
    const navigate = useNavigate()
    const {setToken, setFeedback} = useContext(GlobalContext)

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [admin, setAdmin] = useState(false)

    const gerenciarLogin = async (form, controller) => {

        if (!email || !password) return

        const dadosLogin = {email, password, admin}

        const token = await LoginClient.login(dadosLogin, controller.signal)

        if (!token || token === "") {
            navigate('/usuarios/password')
            return setFeedback({type: 'info', mensagem: 'É necessário atualizar sua senha.'})
        }

        setToken(token)
        navigate('/')
        setFeedback({type: 'success', mensagem: 'Login realizado com sucesso.'})
    }

    return (
        <PaginaAutenticacao onSubmit={gerenciarLogin} submitButton="Entrar" submitButtonDisabled="Entrando...">
            <h2>Entrar</h2>

            <InputEmail email={email} setEmail={setEmail}/>

            <InputSenha senha={password} setSenha={setPassword}/>

            <span className={Styles.admin}>
                <input
                    type="checkbox"
                    defaultChecked={admin}
                    onChange={() => setAdmin(!admin)}
                />
                Acesso Administrador
            </span>

        </PaginaAutenticacao>
    )
}
