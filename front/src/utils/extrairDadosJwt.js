export default function extrairDadosJwt(token) {
    if (!token) return null
    try {
        const parts = token.split('.')
        if (parts.length < 2) return null
        const payload = parts[1]
        const base64 = payload.replace(/-/g, '+').replace(/_/g, '/')
        const pad = base64.length % 4
        const padded = base64 + (pad ? '='.repeat(4 - pad) : '')
        const json = atob(padded)
        return JSON.parse(json)
    } catch {
        return null
    }
}