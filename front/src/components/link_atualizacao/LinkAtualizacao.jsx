import Styles from './styles.module.css'
import Edit from './assets/edit.png'

export default function LinkAtualizacao({setStatusModal, altLink = 'Atualizar'}) {
    return (
        <a
            href="#"
            onClick={() => setStatusModal(true)}
            className={Styles.link}
        >
            <img src={Edit} alt={altLink}/>
        </a>
    )
}