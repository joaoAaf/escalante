import Styles from './styles.module.css'
import FireFighter from './assets/firefighter.png'
import Notes from './assets/notes.png'
import Users from './assets/users.png'
import User from './assets/user.png'
import {NavLink, useNavigate} from 'react-router-dom'
import {useContext, useState} from 'react'
import {GlobalContext} from '../../context/GlobalContext'
import extrairDadosJwt from '../../utils/extrairDadosJwt.js'
import PerfilUsuario from '../perfil_usuario/PerfilUsuario.jsx'
import {obterPerfis} from '../../utils/gerenciadorRedirecionamento.js'

export default function Sidebar() {
    const {token, setFeedback} = useContext(GlobalContext)
    const [abrirPerfil, setAbrirPerfil] = useState(false)
    const navigate = useNavigate()

    const abrirModalPerfil = () => {
        if (!token) return setFeedback({type: 'error', mensagem: 'Usuário não autenticado.'})
        const usuario = usuarioDoToken()
        if (!usuario) return
        setAbrirPerfil(true)
    }

    const usuarioDoToken = () => {
        if (!token) return null
        const claims = extrairDadosJwt(token)
        if (!claims) return null
        const username = claims.sub || null
        const perfis = obterPerfis(claims) || []
        return {username, perfis}
    }

    const usuario = usuarioDoToken()

    const navegarAlterarSenha = () => {
        setAbrirPerfil(false)
        navigate('/password')
    }

    const perfisUsuario = (usuario && usuario.perfis) || []
    const isAdmin = perfisUsuario.includes('ADMIN')
    const isEscalante = perfisUsuario.includes('ESCALANTE')

    return (
        <div className={Styles.sidebar}>
            <h1>Escalante</h1>
            <nav>
                {!isAdmin && (
                    <NavLink
                        to="/militares"
                        title="Militares"
                        className={({isActive}) => isActive ? Styles.active : ''}
                    >
                        <img src={FireFighter} alt="Militares"/>
                        <span>Militares</span>
                    </NavLink>
                )}
                {!isAdmin && (
                    <NavLink
                        to="/escala"
                        title="Escala"
                        className={({isActive}) => isActive ? Styles.active : ''}
                    >
                        <img src={Notes} alt="Escala"/>
                        <span>Escala</span>
                    </NavLink>
                )}
                {!isEscalante && (
                    <NavLink
                        to="/usuarios"
                        title="Usuários"
                        className={({isActive}) => isActive ? Styles.active : ''}
                    >
                        <img src={Users} alt="Usuários"/>
                        <span>Usuários</span>
                    </NavLink>
                )}
            </nav>

            <div className={Styles.userIconContainer} onClick={abrirModalPerfil} role="button" aria-label="Meu perfil">
                <img src={User} alt="Meu Perfil" className={Styles.userIcon}/>
            </div>

            <PerfilUsuario abrir={abrirPerfil} fechar={() => setAbrirPerfil(false)} usuario={usuario}
                           alterarSenha={navegarAlterarSenha}/>
        </div>
    )
}
