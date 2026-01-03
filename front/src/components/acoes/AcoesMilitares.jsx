import Acoes from "./Acoes.jsx";
import GlobalContext from "../../context/GlobalContext.jsx";
import {useContext, useState} from "react";
import CadastroMilitar from '../cadastro/CadastroMilitar.jsx'

export default function AcoesMilitares() {

    const {setReload} = useContext(GlobalContext)
    const [abrirCadastro, setAbrirCadastro] = useState(false)

    return (
        <>
            <Acoes titulo="Ações para Tabela de Militares">
                <button onClick={() => setAbrirCadastro(true)}>Adicionar Militar</button>
                <button onClick={() => setReload(true)}>Recarregar Tabela</button>
            </Acoes>

            <CadastroMilitar abrir={abrirCadastro} fechar={() => setAbrirCadastro(false)} />
        </>
    )
}
