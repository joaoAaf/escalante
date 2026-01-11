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

    static async cadastrarMilitares(militares, token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(militares || []),
                signal
            })

            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possivel cadastrar militares.' : JSON.parse(erro)?.Mensagem || erro)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error('Servidor indisponível.')
            throw error
        }
    }

    static async obterMilitarPorMatricula(matricula, token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/${encodeURIComponent(matricula)}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })

            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possivel obter o militar.' : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.json()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error('Servidor indisponível.')
            throw error
        }
    }

    static async listarMilitares(token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })

            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possivel obter militares.' : JSON.parse(erro)?.Mensagem || erro)
            }

            const texto = await response.text()
            return texto ? JSON.parse(texto) : []
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error('Servidor indisponível.')
            throw error
        }
    }

    static async deletarMilitar(matricula, token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/${encodeURIComponent(matricula)}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possivel deletar o militar.' : JSON.parse(erro)?.Mensagem || erro)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error('Servidor indisponível.')
            throw error
        }
    }

    static async atualizarMilitar(militar, token, signal) {
        try {
            const response = await fetch(`${this.baseUrl}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(militar),
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possivel atualizar o militar.' : JSON.parse(erro)?.Mensagem || erro)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error('Servidor indisponível.')
            throw error
        }
    }

}