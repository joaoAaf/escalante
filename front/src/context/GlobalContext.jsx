import {createContext, useState} from 'react'
import {useLocalStorage} from '../hooks/useLocalStorage'
import {ordenarEscala} from '../utils/ordenacaoEscala'

const normalizarEscala = escala => {
    if (!Array.isArray(escala)) return escala
    return ordenarEscala(escala.map(servico => {
        if (!servico) return servico
        if (servico.funcao === 'Operador de Linha 1')
            return {...servico, funcao: 'Operador de Linha', linha: 1}
        if (servico.funcao === 'Operador de Linha 2')
            return {...servico, funcao: 'Operador de Linha', linha: 2}
        if (servico.funcao === 'Operador de Linha' && servico.linha == null)
            return {...servico, linha: 1}
        return servico
    }))
}

const GlobalContext = createContext()

export const GlobalContextProvider = ({children}) => {

    const [escala, setEscala] = useLocalStorage('escala', normalizarEscala)
    const [militares, setMilitares] = useState([])
    const [token, setToken] = useLocalStorage('token')
    const [reload, setReload] = useState(true)
    const [feedback, setFeedback] = useState(null)

    return (
        <GlobalContext.Provider value={{
            escala,
            setEscala,
            militares,
            setMilitares,
            token,
            setToken,
            reload,
            setReload,
            feedback,
            setFeedback
        }}>
            {children}
        </GlobalContext.Provider>
    )
}

export default GlobalContext
