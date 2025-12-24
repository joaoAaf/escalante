import {createContext, useState} from 'react'
import {useLocalStorage} from '../hooks/useLocalStorage'

export const GlobalContext = createContext()

export const GlobalContextProvider = ({ children }) => {
    
    const [escala, setEscala] = useLocalStorage('escala')
    const [militares, setMilitares] = useLocalStorage('militares')
    const [token, setToken] = useLocalStorage('token')
    const [feedback, setFeedback] = useState(null)

    return (
        <GlobalContext.Provider value={{
            escala,
            setEscala,
            militares,
            setMilitares,
            token,
            setToken,
            feedback,
            setFeedback
        }}>
            {children}
        </GlobalContext.Provider>
    )
}