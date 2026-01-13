export default class LogoutClient {
    static baseUrl = '/api/logout'

    static async logout(token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? "Não foi possivel fazer logout." : JSON.parse(erro)?.Mensagem || erro)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError) throw new Error("Servidor indisponível.")
            throw error
        }
    }
}