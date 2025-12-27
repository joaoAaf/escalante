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
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível atualizar a senha.' : JSON.parse(erro)?.Mensagem || erro)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError) throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async listarUsuarios(token, signal) {
        try {
            const response = await fetch(this.baseUrl, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível listar os usuários.' : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.json()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

}