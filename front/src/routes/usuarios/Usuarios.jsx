import Styles from './styles.module.css'
import { useContext, useEffect, useRef, useState } from 'react'
import BotaoRemover from '../../components/botao_remover/BotaoRemover'
import { GlobalContext } from "../../context/GlobalContext.jsx";
import UsuarioClient from "../../clients/UsuarioClient.js";

export default function Usuarios() {

    const { token, setFeedback } = useContext(GlobalContext)
    const [usuarios, setUsuarios] = useState([])

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

    useEffect(() => {

        const controller = criarAbortController()

        UsuarioClient.listarUsuarios(token, controller.signal)
            .then(usuarios => {
                setUsuarios(usuarios || [])
            })
            .catch(error => {
                if (error.name === 'AbortError') return
                setFeedback({ type: 'error', mensagem: error.message })
            })
            .finally(() => {
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }, [])

    const tabelaUsuarios = () => {
        if (!usuarios || usuarios.length === 0) {
            return (
                <tr>
                    <td colSpan={3}>Nenhum usuário encontrado.</td>
                </tr>
            )
        }
        return usuarios.map(usuario => {
            return (
                <tr key={usuario.username}>
                    <td>{usuario.username}</td>
                    <td>{usuario.perfis.join(' ')}</td>
                    <td>
                        <BotaoRemover
                            tabela={usuarios}
                            setTabela={setUsuarios}
                            id={usuario.username}
                            idKey={'username'}
                            campos={["Email", "Perfil"]}
                        />
                    </td>
                </tr>
            )
        })
    }

    return (
        <div className={Styles.main}>
            <h2>Gerenciamento de Usuários</h2>

            <div className={Styles.acoes}>
                <button onClick={() => alert('Abrir modal de cadastro (exemplo)')}>Cadastrar Usuário</button>
            </div>
            <table className={Styles.table}>
                <thead>
                    <tr>
                        <th>EMAIL</th>
                        <th>PERFIL</th>
                        <th>AÇÕES</th>
                    </tr>
                </thead>
                <tbody>
                    {tabelaUsuarios()}
                </tbody>
            </table>
        </div>
    )
}
