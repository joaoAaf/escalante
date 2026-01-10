import {useContext, useEffect} from 'react'
import GlobalContext from '../../context/GlobalContext'
import Styles from './styles.module.css'

export default function FeedbackToast() {

    const {feedback, setFeedback} = useContext(GlobalContext)

    const fecharToast = () => {
        setFeedback(null)
    }

    useEffect(() => {
        if (feedback) {
            const timer = setTimeout(() => {
                setFeedback(null)
            }, 3000)
            return () => clearTimeout(timer)
        }

    }, [feedback])

    if (!feedback) return null

    const toastClass = {
        success: Styles.success,
        error: Styles.error,
        info: Styles.info
    }[feedback.type] || Styles.info


    return (
        <div className={Styles.toastContainer}>
            <div className={`${Styles.toast} ${toastClass}`}>
                <span>{feedback.mensagem}</span>
                <button className={Styles.clearBtn} onClick={fecharToast} title="Fechar">✕</button>
            </div>
        </div>
    )

}
