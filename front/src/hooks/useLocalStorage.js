import { useEffect, useState } from 'react'

const localStorageDisponivel = () => {
    try {
        const testKey = '__test__'
        localStorage.setItem(testKey, testKey)
        localStorage.removeItem(testKey)
        return true
    } catch (e) {
        console.warn("localStorage não está disponível:", e)
        return false
    }
}

const LOCAL_STORAGE_DISPONIVEL = localStorageDisponivel()

export const useLocalStorage = chave => {

    const [dados, setDados] = useState(() => {
        if (!LOCAL_STORAGE_DISPONIVEL) {
            return null
        }
        try {
            const dadosSalvos = localStorage.getItem(chave)
            return dadosSalvos ? JSON.parse(dadosSalvos) : null
        } catch (error) {
            console.error("Erro ao carregar dados do localStorage:", error)
            return null
        }
    })

    useEffect(() => {
        if (!LOCAL_STORAGE_DISPONIVEL) {
            return
        }
        try {
            if (dados === null || dados === undefined)
                localStorage.removeItem(chave)
            else
                localStorage.setItem(chave, JSON.stringify(dados))
        } catch (error) {
            console.error("Erro ao salvar dados no localStorage:", error)
        }
    }, [dados])

    return [dados, setDados]

}