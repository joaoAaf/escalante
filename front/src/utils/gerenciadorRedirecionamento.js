import extrairDadosJwt from './extrairDadosJwt.js'

export function obterPerfis(claims) {
    if (!claims) return []
    const perfis = claims?.scope || []
    return perfis.map(s => String(s).toUpperCase()) || []
}

export function redirecionar(token) {
    if (!token) return { path: '/login', reason: 'no-token' }
    const claims = extrairDadosJwt(token)
    if (!claims) return { path: '/login', reason: 'invalid' }

    if (claims.exp && Number.isFinite(claims.exp)) {
        if (claims.exp * 1000 <= Date.now()) return { path: '/login', reason: 'expired' }
    }

    const perfis = obterPerfis(claims)
    const isAdmin = perfis.includes('ADMIN')
    const isEscalante = perfis.includes('ESCALANTE')

    if (isAdmin) return { path: '/usuarios', reason: 'admin' }
    if (isEscalante) return { path: '/militares', reason: 'escalante' }

    return { path: '/login', reason: 'no-permission' }
}
