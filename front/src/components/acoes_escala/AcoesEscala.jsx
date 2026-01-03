import {useContext, useEffect, useRef, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import CadastroServicoContext from '../../context/CadastroServicoContext'
import Styles from './styles.module.css'
import EscalaClient from '../../clients/EscalaClient'

export default function AcoesEscala() {

    const {token, escala, setFeedback} = useContext(GlobalContext)
    const {setStatusModal} = useContext(CadastroServicoContext)

    const [exportandoEscala, setExportandoEscala] = useState(false)

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

    const exportarEscalaXLSX = escala => {
        if (!escala || escala.length === 0)
            return setFeedback({type: 'info', mensagem: 'Não há escala disponível para exportação.'})

        if (escala.length > 210)
            return setFeedback({type: 'info', mensagem: 'A escala não pode conter mais que 210 serviços.'})

        const controller = criarAbortController()

        setExportandoEscala(true)
        EscalaClient.exportarEscalaXLSX(escala, token, controller.signal)
            .then(arrayBuffer => {
                if (arrayBuffer) {
                    const blob = new Blob([arrayBuffer],
                        {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'})
                    const url = URL.createObjectURL(blob)
                    const link = document.createElement('a')
                    link.href = url
                    link.download = `escala_${new Date().toLocaleDateString('sv-SE')}.xlsx`
                    link.click()
                    URL.revokeObjectURL(url)
                }
                setFeedback({
                    type: 'success',
                    mensagem: 'Exportação da escala realizada com sucesso. Download iniciado.'
                })
            })
            .catch(error => {
                if (error.name === 'AbortError') return
                setFeedback({type: 'error', mensagem: error.message})
            })
            .finally(() => {
                setExportandoEscala(false)
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }

    return (
        <div className={Styles.acoesEscala}>
            <h3>Ações para Escala Criada</h3>
            <div>
                <button onClick={() => setStatusModal(true)}>Adicionar Serviço</button>
                <button
                    onClick={() => exportarEscalaXLSX(escala)}
                    disabled={exportandoEscala}
                >
                    {exportandoEscala ? "Exportando..." : "Exportar Escala"}
                </button>
            </div>
        </div>
    )
}
