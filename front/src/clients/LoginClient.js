export default class LoginClient {

    static baseUrl = '/api/login'

    static async login(dadosLogin, signal) {
        const { email, password } = dadosLogin || {}
        try {
            const credentials = btoa(`${email}:${password}`)
            const response = await fetch(`${this.baseUrl}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Basic ${credentials}`
                },
                signal
            })

            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? "Não foi possivel fazer login." : JSON.parse(erro)?.Mensagem || erro)
            }
            const dados = await response.json()
            return dados.bearerToken
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError) throw new Error("Servidor indisponível.")
            throw error
        }
    }
}