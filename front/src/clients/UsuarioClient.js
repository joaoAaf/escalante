export default class UsuarioClient {

    static baseUrl = '/api/usuarios'

    static async atualizarPassword(dadosAtualizacao, signal) {
        const {email, password, newPassword} = dadosAtualizacao || {}
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

    static async cadastrarUsuario(token, usuarioRequest, signal) {
        try {
            const response = await fetch(this.baseUrl, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuarioRequest),
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível cadastrar o usuário.' : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.json()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async deletarUsuario(token, username, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/${encodeURIComponent(username)}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível deletar o usuário.' : JSON.parse(erro)?.Mensagem || erro)
            }
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async adicionarPerfis(token, usuarioRequest, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/perfis/adicionar`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuarioRequest),
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível adicionar os perfis.' : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.json()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async removerPerfis(token, usuarioRequest, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/perfis/remover`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuarioRequest),
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível remover os perfis.' : JSON.parse(erro)?.Mensagem || erro)
            }
            return await response.json()
        } catch (error) {
            if (error.name === 'AbortError') throw error
            if (error instanceof TypeError || error instanceof SyntaxError)
                throw new Error("Servidor indisponível.")
            throw error
        }
    }

    static async atualizarUsername(token, username, signal) {
        try {
            const response = await fetch(`${this.baseUrl}/username?novo=${encodeURIComponent(username)}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                signal
            })
            if (!response.status.toString().startsWith('2')) {
                const erro = await response.text()
                throw new Error(!erro ? 'Não foi possível atualizar o username.' : JSON.parse(erro)?.Mensagem || erro)
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