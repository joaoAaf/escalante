import {useContext, useEffect, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import ModalLargo from '../modal/ModalLargo.jsx'
import BotoesModal from '../modal/BotoesModal'
import TabelaMilitares from '../tabela_militares/TabelaMilitares'
import MilitarClient from '../../clients/MilitarClient'
import Styles from './styles.module.css'

export default function CadastroListaMilitares({abrir, fechar, militaresImportados, onSucesso}) {
    const {token, setFeedback} = useContext(GlobalContext)
    const [salvando, setSalvando] = useState(false)

    const [listaEditavel, setListaEditavel] = useState([])

    useEffect(() => {
        if (abrir) setListaEditavel(Array.isArray(militaresImportados) ? militaresImportados : [])
    }, [abrir, militaresImportados])

    const podeSalvar = Array.isArray(listaEditavel) && listaEditavel.length > 0

    const salvar = async () => {
        if (!podeSalvar) {
            setFeedback({type: 'info', mensagem: 'Não há militares para salvar.'})
            return
        }

        try {
            setSalvando(true)
            await MilitarClient.cadastrarListaMilitares(listaEditavel, token)
            setFeedback({type: 'success', mensagem: 'Militares cadastrados com sucesso.'})
            fechar()
            onSucesso?.()
        } catch (error) {
            setFeedback({type: 'error', mensagem: error.message})
        } finally {
            setSalvando(false)
        }
    }

    return (
        <ModalLargo abrir={abrir} fechar={fechar} titulo="Revisar Militares">
            <p className={Styles.tip}>
                Revise a lista importada. Você pode marcar/desmarcar C.O.V. e remover itens antes de salvar.
            </p>

            <div className={Styles.tabelaScroll}>
                <TabelaMilitares tabela={listaEditavel} setTabela={setListaEditavel}/>
            </div>

            <BotoesModal confirmar={salvar} cancelar={fechar} textoConfirmar="Salvar"/>

            {salvando && <span className={Styles.status}>Salvando...</span>}
        </ModalLargo>
    )
}
