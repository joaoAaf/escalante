import {useContext, useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import UsuarioClient from "../../clients/UsuarioClient.js";
import PaginaAutenticacao from '../../components/pagina_autenticacao/PaginaAutenticacao'
import InputSenha from "../../components/input_senha/InputSenha.jsx";
import InputEmail from "../../components/input_email/InputEmail.jsx";

export default function AtualizacaoSenha() {
    const navigate = useNavigate()
    const {setToken, setFeedback} = useContext(GlobalContext)

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [novoPassword, setNovoPassword] = useState('')
    const [confirmarNovoPassword, setConfirmarNovoPassword] = useState('')

    const gerenciarAtualizacaoSenha = async (form, controller) => {
        if (!email || !password || !novoPassword || !confirmarNovoPassword) return

        if (novoPassword !== confirmarNovoPassword)
            return setFeedback({type: 'info', mensagem: 'As senhas não coincidem. Por favor, verifique.'})

        const dadosAtualizacao = {email, password, novoPassword}

        await UsuarioClient.atualizarPassword(dadosAtualizacao, controller.signal)
        setToken("")
        navigate('/login')
        setFeedback({type: 'success', mensagem: 'Senha atualizada com sucesso.'})
    }

    return (
        <PaginaAutenticacao onSubmit={gerenciarAtualizacaoSenha} submitButton="Atualizar"
                            submitButtonDisabled="Atualizando...">
            <h2>Atualizar Senha</h2>

            <InputEmail email={email} setEmail={setEmail}/>

            <InputSenha senha={password} setSenha={setPassword} label="Senha Atual:"/>

            <InputSenha senha={novoPassword} setSenha={setNovoPassword} label="Nova Senha:"/>

            <InputSenha senha={confirmarNovoPassword} setSenha={setConfirmarNovoPassword} label="Confirmar Senha:"/>

        </PaginaAutenticacao>
    )
}
