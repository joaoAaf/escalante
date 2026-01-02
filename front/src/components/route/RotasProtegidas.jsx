import {useContext, useEffect, useState} from 'react'
import {Navigate} from 'react-router-dom'
import {GlobalContext} from '../../context/GlobalContext'
import extrairDadosJwt from '../../utils/extrairDadosJwt.js'
import {obterPerfis, redirecionar} from '../../utils/gerenciadorRedirecionamento.js'

export default function RotasProtegidas({perfisPermitidos = [], children}) {
    const {token, setToken, setFeedback} = useContext(GlobalContext)
    const [permitido, setPermitido] = useState(null)

    useEffect(() => {
        if (!token) {
            setPermitido(false)
            return
        }
        const claims = extrairDadosJwt(token)
        if (!claims) {
            setToken('')
            setPermitido(false)
            return
        }
        if (claims.exp && Number.isFinite(claims.exp) && claims.exp * 1000 <= Date.now()) {
            setToken('')
            setFeedback({type: 'error', mensagem: 'Sessão expirada. Faça login novamente.'})
            setPermitido(false)
            return
        }
        const perfis = obterPerfis(claims)
        if (!perfisPermitidos || perfisPermitidos.length === 0) {
            setPermitido(true)
            return
        }
        const normalized = perfis.map(s => s.toUpperCase())
        const ok = perfisPermitidos.some(a => normalized.includes(a.toUpperCase()))
        setPermitido(ok)
    }, [token, perfisPermitidos, setToken, setFeedback])

    if (permitido === null) return null

    if (permitido) return children

    const {path} = redirecionar(token)
    return <Navigate to={path} replace/>
}

