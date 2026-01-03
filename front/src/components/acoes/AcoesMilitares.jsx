import Acoes from "./Acoes.jsx";
import GlobalContext from "../../context/GlobalContext.jsx";
import {useContext} from "react";

export default function AcoesMilitares() {

    const {setReload} = useContext(GlobalContext)

    return (
        <Acoes titulo="Ações para Tabela de Militares">
            <button onClick={() => {}}>Adicionar Militar</button>
            <button onClick={() => setReload(true)}>Recarregar Tabela</button>
        </Acoes>
    )
}
