export default class EscalaClient {

    static baseUrl = '/api/escala'

    static async criarEscalaAutomatica(dadosEscala, token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(dadosEscala),
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? "Não foi possivel criar escala." : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.json()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async exportarEscalaXLSX(escala, token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/exportar/xlsx`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(escala),
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? "Não foi possivel exportar escala." : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.arrayBuffer()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async obterPlanilhaModeloEscala(token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/modelo/xlsx`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? "Não foi possivel obter modelo." : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.arrayBuffer()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async importarEscalaXLSX(arquivo, token, signal) {
        const formData = new FormData()
        formData.append('escala', arquivo)
        try {
            const response = await fetch(`${this.baseUrl}/importar/xlsx`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                body: formData,
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? "Não foi possivel importar planilha." : JSON.parse(erro)?.Mensagem || erro)
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