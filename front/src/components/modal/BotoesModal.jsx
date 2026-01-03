import Styles from './styles.module.css'

export default function BotoesModal({typeConfirmar = 'button', confirmar, cancelar, textoConfirmar = "Confirmar"}) {

    if (typeConfirmar === 'submit') {
        textoConfirmar = "Salvar"
        confirmar = null
    }

    return (
        <footer className={Styles.botoesModal}>
            <button type={typeConfirmar} onClick={confirmar} className={Styles.confirmar}>{textoConfirmar}</button>
            <button type="button" onClick={cancelar} className={Styles.cancelar}>Cancelar</button>
        </footer>
    )
}