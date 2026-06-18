import Styles from './styles.module.css'
import Delete from '../botao_remover/assets/delete.png'

export default function BotoesModal({
                                        typeConfirmar = 'button',
                                        confirmar,
                                        cancelar,
                                        textoConfirmar = "Confirmar",
                                        isNovo = false,
                                        handleRemover,
                                        carregando = false
                                    }) {

    if (typeConfirmar === 'submit') {
        textoConfirmar = "Salvar"
        confirmar = null
    }

    return (
        <footer className={Styles.botoesModal}>
                {isNovo && (
                    <button
                        onClick={handleRemover}
                        className={Styles.remover}
                    >
                        <img src={Delete} alt="Remover" />
                    </button>
                )}
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