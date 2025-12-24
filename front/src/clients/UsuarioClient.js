export default class UsuarioClient {

    static baseUrl = '/api/usuarios'

    static async atualizarPassword(dadosAtualizacao, signal) {
        const { email, password, newPassword } = dadosAtualizacao || {}
        try {
            const credentials = btoa(`${email}:${password}`)
            const response = await fetch(`${this.baseUrl}/password?novo=${encodeURIComponent(newPassword)}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Basic ${credentials}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const dados = await response.json()
                throw new Error(dados?.Mensagem || dados)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError) throw new Error("Servidor indisponível.")
            throw error
        }
    }
}