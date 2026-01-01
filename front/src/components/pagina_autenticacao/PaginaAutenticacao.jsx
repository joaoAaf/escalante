import Styles from './styles.module.css'
import {useContext, useEffect, useRef, useState} from 'react'
import {GlobalContext} from '../../context/GlobalContext'

export default function PaginaAutenticacao({children, onSubmit, submitButton, submitButtonDisabled}) {
    const {setFeedback} = useContext(GlobalContext)
    const [carregando, setCarregando] = useState(false)
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

    const gerenciarForm = async (evento) => {
        evento.preventDefault()
        const form = evento.currentTarget

        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        const controller = criarAbortController()
        setCarregando(true)

        try {
            await onSubmit(form, controller)
        } catch (error) {
            if (error && error.name === 'AbortError') return
            setFeedback({type: 'error', mensagem: error?.message || String(error)})
        } finally {
            setCarregando(false)
            if (abortControllerRef.current === controller)
                abortControllerRef.current = null
        }
    }

    return (
        <div className={Styles.page}>
            <h1 className={Styles.title}>Escalante</h1>
            <div className={Styles.container}>
                <form onSubmit={gerenciarForm} noValidate>
                    {children}
                    <button type="submit" disabled={carregando}>
                        {carregando ? submitButtonDisabled : submitButton}
                    </button>
                </form>
            </div>
        </div>
    )
}