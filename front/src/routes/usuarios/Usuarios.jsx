import Styles from './styles.module.css'
import { useState } from 'react'
import BotaoRemover from '../../components/botao_remover/BotaoRemover'

export default function Usuarios() {
    const [usuarios, setUsuarios] = useState([
        { email: 'admin@example.com', perfil: 'ADMIN' },
        { email: 'user1@example.com', perfil: 'USER' },
        { email: 'user2@example.com', perfil: 'USER' }
    ])

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
                        {usuarios.map((u, i) => (
                            <tr key={i}>
                                <td>{u.email}</td>
                                <td>{u.perfil}</td>
                                <td>
                                    <BotaoRemover
                                        tabela={usuarios}
                                        setTabela={setUsuarios}
                                        id={u.email}
                                        idKey={'email'}
                                        campos={["Email", "Perfil"]}
                                    />
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
        </div>
    )
}
