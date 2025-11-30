export default class EscalaClient {

    static baseUrl = `${import.meta.env.VITE_API_URL ?? ''}/api/escala`

    static async criarEscalaAutomatica(dadosEscala, signal) {
        try {
            const response = await fetch(`${this.baseUrl}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosEscala),
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

    static async exportarEscalaXLSX(escala, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/exportar/xlsx`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(escala),
                signal
            })
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

    static async obterPlanilhaModeloEscala(signal) {
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

    static async importarEscalaXLSX(arquivo, signal) {
        const formData = new FormData()
        formData.append('escala', arquivo)
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