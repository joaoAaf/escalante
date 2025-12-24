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
            const dados = await response.json()
            if (!response.status.toString().startsWith('2')) throw new Error(dados.Mensagem)
            return dados.bearerToken
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError) throw new Error("Servidor indisponível.")
            throw error
        }
    }
}