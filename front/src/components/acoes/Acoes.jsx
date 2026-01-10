import Styles from './styles.module.css'

export default function Acoes({children, titulo}) {
    return (
        <div className={Styles.acoes}>
            <h3>{titulo}</h3>
            <div>
                {children}
            </div>
        </div>
    )
}
