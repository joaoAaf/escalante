import Styles from './styles.module.css'

export default function BotoesModal({
                                        typeConfirmar = 'button',
                                        confirmar,
                                        cancelar,
                                        textoConfirmar = "Confirmar",
                                        carregando = false
                                    }) {

    if (typeConfirmar === 'submit') {
        textoConfirmar = "Salvar"
        confirmar = null
    }

    return (
        <footer className={Styles.botoesModal}>
            <button
                className={Styles.confirmar}
                type={typeConfirmar}
                onClick={confirmar}
                disabled={carregando}
            >{carregando ? "Processando..." : textoConfirmar}</button>
            <button type="button" onClick={cancelar} className={Styles.cancelar}>Cancelar</button>
        </footer>
    )
}