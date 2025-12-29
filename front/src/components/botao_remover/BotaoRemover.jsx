import { useContext, useState } from 'react'
import { GlobalContext } from '../../context/GlobalContext'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'
import Delete from './assets/delete.png'
import Styles from './styles.module.css'
import { formatarData } from '../../utils/formatarData'

export default function BotaoRemover({ tabela, setTabela, id, idKey, campos, apiRemover }) {

    const { setFeedback } = useContext(GlobalContext)
    const [statusModal, setStatusModal] = useState(false)
    const [confirmacao, setConfirmacao] = useState([])

    const invocarModal = evento => {
        evento.preventDefault()

        if (id === undefined || id === null) {
            setFeedback({ type: 'error', mensagem: 'ID inválido para remoção.' })
            return
        }

        const item = (tabela || []).find(it => String(it?.[idKey]) === String(id))

        if (!item) {
            setFeedback({ type: 'error', mensagem: 'Item não encontrado para remoção.' })
            return
        }

        const valores = Object.values({ ...item, dataServico: formatarData(item.dataServico), nascimento: formatarData(item.nascimento) })

        const mapaCamposValores = campos.map((campo, index) => {
            if (valores[index] === 0) return [campo, '0']
            if (valores[index] === false) return [campo, 'Não']
            if (valores[index] === true) return [campo, 'Sim']
            return [campo, valores[index] || '']
        })

        setConfirmacao(mapaCamposValores)
        setStatusModal(true)
    }

    const removerItem = async () => {
        setStatusModal(false)
        if (apiRemover) {
            try {
                await apiRemover(id)
            } catch (error) {
                if (error.name === 'AbortError') return
                setFeedback({ type: 'error', mensagem: error.message })
                return
            }
        }
        const novaTabela = (tabela || []).filter(item => String(item?.[idKey]) !== String(id))
        setTabela(novaTabela)
        setFeedback({ type: 'success', mensagem: 'Item removido com sucesso.' })
    }

    return (
        <>
            <a
                href="#"
                onClick={e => invocarModal(e)}
                className={Styles.botaoRemover}
            >
                <img src={Delete} alt="Remover" />
            </a>

            <Modal abrir={statusModal} fechar={() => setStatusModal(false)} titulo="Confirmar Remoção">
                <p className={Styles.textoConfirmacao}>
                    {`Tem certeza que deseja remover o item abaixo?`}
                </p>
                <table>
                    <tbody>
                        {confirmacao.map(([chave, valor], index) => (
                            <tr key={index}>
                                <td><strong>{chave}</strong></td>
                                <td>{valor}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <BotoesModal confirmar={removerItem} cancelar={() => setStatusModal(false)} />
            </Modal>
        </>
    )

}
