export default class EscalaClient {

    static baseUrl = `${import.meta.env.VITE_API_URL ?? 'http://localhost:8080'}/api/escala`

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
            if (!response.status.toString().startsWith('2'))
                throw new Error(`Erro ao criar escala: ${response.status} ${response.statusText}`)
            return await response.json()
        } catch (error) {
            console.error(error.message)
            if (error.name === 'AbortError') throw error
            throw new Error("Erro ao criar escala: Servidor indisponível.")
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
                throw new Error(`Erro ao exportar escala: ${response.status} ${response.statusText}`)
            return await response.arrayBuffer()
        } catch (error) {
            console.error(error.message)
            if (error.name === 'AbortError') throw error
            throw new Error("Erro ao exportar escala: Servidor indisponível.")
        }
    }

    static async obterPlanilhaModeloEscala(signal) {
        try {
            const response = await fetch(`${this.baseUrl}/modelo/xlsx`, { signal })
            if (!response.status.toString().startsWith('2'))
                throw new Error(`Erro ao obter modelo de escala: ${response.status} ${response.statusText}`)
            return await response.arrayBuffer()
        } catch (error) {
            console.error(error.message)
            if (error.name === 'AbortError') throw error
            throw new Error("Erro ao obter modelo: Servidor indisponível.")
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
            if (!response.status.toString().startsWith('2'))
                throw new Error(`Erro ao importar escala: ${response.status} ${response.statusText}`)
            return await response.json()
        } catch (error) {
            console.error(error.message)
            if (error.name === 'AbortError') throw error
            throw new Error("Erro ao importar escala: Servidor indisponível.")
        }
    }

}