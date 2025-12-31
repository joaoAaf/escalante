import {useContext, useState} from 'react'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'
import Styles from './styles.module.css'
import UsuarioClient from '../../clients/UsuarioClient'
import {GlobalContext} from '../../context/GlobalContext'
import RespostaCadastroUsuario from './resposta_cadastro/RespostaCadastroUsuario.jsx'
import InputEmail from "../input_email/InputEmail.jsx";
import InputPerfis from "../input_perfis/InputPerfis.jsx";

export default function CadastroUsuario({abrir, fechar, setUsuarios}) {
    const {token, setFeedback} = useContext(GlobalContext)
    const [email, setEmail] = useState('')
    const [perfis, setPerfis] = useState([])
    const [usuarioCriado, setUsuarioCriado] = useState(null)
    const [abrirResultado, setAbrirResultado] = useState(false)

    const cadastrar = async evento => {
        evento.preventDefault()
        const form = evento.currentTarget

        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        if (!email) return

        if (!perfis || perfis.length === 0) {
            return setFeedback({type: 'info', mensagem: 'Adicione pelo menos um perfil ao usuário.'})
        }

        try {
            const controller = new AbortController()
            const usuarioRequest = {username: email, perfis}
            const usuarioSalvo = await UsuarioClient.cadastrarUsuario(token, usuarioRequest, controller.signal)
            setUsuarios(prev => [...(prev || []), usuarioSalvo])
            setEmail('')
            setPerfis([])
            fechar()
            setUsuarioCriado(usuarioSalvo)
            setAbrirResultado(true)
            setFeedback({type: 'success', mensagem: 'Usuário cadastrado com sucesso.'})
        } catch (error) {
            if (error.name === 'AbortError') return
            setFeedback({type: 'error', mensagem: error.message})
        }
    }

    return (
        <>
        <Modal abrir={abrir} fechar={fechar} titulo="Cadastrar Usuário">
            <form onSubmit={cadastrar} className={Styles.CadastroUsuario} noValidate>

                <InputEmail email={email} setEmail={setEmail} />

                <InputPerfis perfis={perfis} setPerfis={setPerfis} />

                <BotoesModal
                    typeConfirmar="submit"
                    cancelar={fechar}
                />

            </form>
        </Modal>

        <RespostaCadastroUsuario abrir={abrirResultado} fechar={() => { setAbrirResultado(false); setUsuarioCriado(null); }} usuario={usuarioCriado} />
        </>
    )
}
