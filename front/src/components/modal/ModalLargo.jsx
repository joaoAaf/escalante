import styles from './styles.module.css'

export default function ModalLargo({ abrir, fechar, titulo, children }) {
    if (!abrir) return null

    return (
        <div className={styles.modal} onClick={fechar}>
            <div className={styles.dialogWide} onClick={e => e.stopPropagation()}>
                <h2>{titulo}</h2>
                {children}
            </div>
        </div>
    )
}

