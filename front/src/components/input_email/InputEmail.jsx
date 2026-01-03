import Styles from './styles.module.css'
import GlobalContext from "../../context/GlobalContext.jsx";
import {useContext} from "react";

export default function InputEmail({email, setEmail, label = 'Email:'}) {

    const {setFeedback} = useContext(GlobalContext)

    const validarEmail = email => {
        const emailRegex = /^(?=.{1,64}@)[A-Za-z0-9._-]+@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/
        if (!emailRegex.test(email)) {
            setFeedback({type: 'info', mensagem: 'Por favor, digite um email válido.'})
            return ""
        }
        return email
    }

    return (
        <>
            <label className={Styles.label}>{label}</label>
            <input
                className={Styles.input}
                type="email"
                value={email}
                onChange={e => {
                    e.target.setCustomValidity('');
                    setEmail(e.target.value)
                }}
                onBlur={e => setEmail(validarEmail(e.target.value))}
                required
                placeholder="email@exemplo.com"
                maxLength={130}
                onInvalid={e => e.target.setCustomValidity('Por favor, digite um email válido.')}
            />
        </>
    )
}