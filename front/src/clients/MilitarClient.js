export default class MilitarClient {

    static baseUrl = '/api/militar'

    static async obterPlanilhaModeloMilitares(signal) {
        try {
            const response = await fetch(`${this.baseUrl}/modelo/xlsx`, { signal })
            if (!response.status.toString().startsWith('2'))
                throw new Error((await response.json()).Mensagem)
            return await response.arrayBuffer()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async importarMilitaresXLSX(arquivo, signal) {
        const formData = new FormData()
        formData.append('militares', arquivo)
        try {
            const response = await fetch(`${this.baseUrl}/importar/xlsx`, {
                method: 'POST',
                body: formData,
                signal
            })
            const dados = await response.json()
            if (!response.status.toString().startsWith('2'))
                throw new Error(dados.Mensagem)
            return dados
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }
}