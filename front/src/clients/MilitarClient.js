export default class MilitarClient {

    static baseUrl = '/api/militar'

    static async obterPlanilhaModeloMilitares(token, signal) {
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

    static async importarMilitaresXLSX(arquivo, token, signal) {
        const formData = new FormData()
        formData.append('militares', arquivo)
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