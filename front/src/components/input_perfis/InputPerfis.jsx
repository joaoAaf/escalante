import Styles from './styles.module.css'
import GlobalContext from "../../context/GlobalContext.jsx";
import {useContext, useState} from "react";

export default function InputPerfis({perfis, setPerfis}) {

    const {setFeedback} = useContext(GlobalContext)
    const [perfilSelect, setPerfilSelect] = useState('')


    const adicionarPerfil = () => {
        if (!perfilSelect) return setFeedback({type: 'info', mensagem: 'Selecione um perfil para adicionar.'})
        if (perfis.includes(perfilSelect)) return setFeedback({type: 'info', mensagem: 'Perfil já adicionado.'})
        setPerfis([...perfis, perfilSelect])
        setPerfilSelect('')
    }

    const removerPerfil = perfil => setPerfis(perfis.filter(p => p !== perfil))

    return (
        <>
            <label className={Styles.label}>Perfis selecionados:</label>

            <div className={Styles.perfilSelect}>
                <select
                    className={Styles.select}
                    value={perfilSelect}
                    onChange={e => {
                        e.target.setCustomValidity('');
                        setPerfilSelect(e.target.value)
                    }}
                >
                    <option value="" disabled>Selecione o Perfil</option>
                    <option>ADMIN</option>
                    <option>ESCALANTE</option>
                </select>
                <button type="button" onClick={adicionarPerfil}>Adicionar</button>
            </div>

            <div className={Styles.perfis}>
                {perfis.map(p => (
                    <span key={p}>
                        {p}
                        <button type="button" className={Styles.clearBtn}
                                onClick={() => removerPerfil(p)}>x</button>
                    </span>))}
            </div>
            <input type="text" readOnly value={perfis.join(', ')} hidden/>
        </>
    )
}