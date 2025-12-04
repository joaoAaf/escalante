import { useContext, useEffect, useRef, useState } from 'react'
import { GlobalContext } from '../../context/GlobalContext'
import Styles from './styles.module.css'

export default function InputUpload({ funcaoDownload, funcaoUpload, nomeModelo, setDados }) {

    const { setFeedback } = useContext(GlobalContext)
    
    const [nomeArquivo, setNomeArquivo] = useState("Nenhuma seleção.")
    const [arquivo, setArquivo] = useState(null)
    const [carregando, setCarregando] = useState(false)
    const [baixando, setBaixando] = useState(false)

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

    const gerenciarArquivo = evento => {
        if (evento.target.files.length > 0) {
            setNomeArquivo(evento.target.files[0].name)
            setArquivo(evento.target.files[0])
        }
    }

    const enviarArquivo = () => {
        if (arquivo) {

            const controller = criarAbortController()

            setCarregando(true)
            funcaoUpload(arquivo, controller.signal)
                .then(dados => {
                    setDados(dados || [])
                    setFeedback({ type: 'success', mensagem: 'Planilha importada com sucesso.' })
                })
                .catch(error => {
                    if (error.name === 'AbortError') return
                    setFeedback({ type: 'error', mensagem: error.message })
                })
                .finally(() => {
                    setCarregando(false)
                    if (abortControllerRef.current === controller)
                        abortControllerRef.current = null
                })
        } else
            setFeedback({ type: 'info', mensagem: 'Por favor, selecione um arquivo antes de enviar.' })
    }

    const baixarModelo = () => {

        const controller = criarAbortController()

        setBaixando(true)
        funcaoDownload(controller.signal)
            .then(arrayBuffer => {
                if (arrayBuffer) {
                    const blob = new Blob([arrayBuffer],
                        { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    const url = URL.createObjectURL(blob)
                    const link = document.createElement('a')
                    link.href = url
                    link.download = nomeModelo || 'modelo.xlsx'
                    link.click()
                    URL.revokeObjectURL(url)
                }
                setFeedback({ type: 'success', mensagem: 'Download do modelo iniciado.' })
            })
            .catch(error => {
                if (error.name === 'AbortError') return
                setFeedback({ type: 'error', mensagem: error.message })
            })
            .finally(() => {
                setBaixando(false)
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }

    return (
        <div className={Styles.input_upload}>
            <button
                className={Styles.modelo_btn}
                onClick={baixarModelo}
                disabled={baixando}
            >
                {baixando ? "Baixando..." : "Modelo"}
            </button>

            <input
                type="file"
                id="input_upload"
                onChange={gerenciarArquivo}
                hidden
                accept=".xlsx"
            />

            <label htmlFor="input_upload" className={Styles.browse_btn}>Importar</label>

            <span className={Styles.file_name}>{nomeArquivo}</span>

            <button
                className={Styles.upload_btn}
                onClick={enviarArquivo}
                disabled={carregando}
            >
                {carregando ? "Carregando..." : "Enviar/Processar"}
            </button>
        </div>
    )
}
