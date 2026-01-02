import {useContext, useEffect, useState} from 'react'
import {Navigate, useLocation} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import {redirecionar} from '../../utils/gerenciadorRedirecionamento.js'

export default function RotasGateway() {
    const {token, setToken, setFeedback} = useContext(GlobalContext)
    const [dest, setDest] = useState(null)
    const location = useLocation()

    useEffect(() => {
        const { path, reason } = redirecionar(token)
        if (path === '/login') {
            if (token) setToken('')
            if (reason === 'expired') setFeedback({ type: 'error', mensagem: 'Sessão expirada. Faça login novamente.' })
        }
        setDest(path)
    }, [token, setToken, setFeedback, location.pathname])

    if (!dest) return null

    return <Navigate to={dest} replace />
}

