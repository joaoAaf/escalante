import Styles from './styles.module.css'
import {GlobalContext} from "../../context/GlobalContext.jsx";
import {useContext} from "react";

export default function InputSenha({senha, setSenha, label = 'Senha:'}) {

    const {setFeedback} = useContext(GlobalContext)

    const validarSenha = senha => {
        const senhaRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{9,50}$/
        if (!senhaRegex.test(senha)) {
            setFeedback({type: 'info', mensagem: 'Por favor, digite uma senha válida.'})
            return ""
        }
        return senha
    }

    return (
        <>
            <label className={Styles.label}>{label}</label>
            <input
                className={Styles.input}
                type="password"
                value={senha}
                onChange={e => {
                    e.target.setCustomValidity('');
                    setSenha(e.target.value)
                }}
                onBlur={e => setSenha(validarSenha(e.target.value))}
                placeholder="********"
                required
                minLength="9"
                maxLength="50"
                title="A senha deve conter entre 9 e 50 caracteres, pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial."
                onInvalid={e => e.target.setCustomValidity("Por favor, digite uma senha válida.")}
            />
        </>
    )
}