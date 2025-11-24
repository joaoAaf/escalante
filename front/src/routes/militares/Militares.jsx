import Styles from './styles.module.css'
import { useEffect, useState, useCallback, useContext } from 'react'
import { GlobalContext } from '../../context/GlobalContext'
import BarraPesquisa from '../../components/barra_pesquisa/BarraPesquisa'
import InputUpload from '../../components/input_upload/InputUpload'
import TabelaMilitares from '../../components/tabela_militares/TabelaMilitares'
import MilitarClient from '../../clients/MilitarClient'

export default function Militares() {

    const { militares, setMilitares } = useContext(GlobalContext)

    const [militaresFiltrados, setMilitaresFiltrados] = useState(null)
    const [ultimaPesquisa, setUltimaPesquisa] = useState(null)

    const camposPesquisa = [
        { value: 'nome', label: 'Nome de Paz' },
        { value: 'matricula', label: 'Matrícula' },
        { value: 'cov', label: 'C.O.V.', disableInput: true }
    ]

    const normalize = v => String(v ?? '').toLowerCase()

    const gerenciarPesquisa = useCallback(({ campo, consulta }) => {
        setUltimaPesquisa(pesquisa => {
            if (pesquisa && pesquisa.campo === campo && pesquisa.consulta === consulta) return pesquisa
            return { campo, consulta }
        })

        const q = String(consulta ?? '').trim().toLowerCase()

        if (!militares) return

        if (campo === 'cov') {
            const resultadosCov = militares.filter(m => m.cov === true)
            setMilitaresFiltrados(resultadosCov)
            return
        }

        if (!q) {
            setMilitaresFiltrados(militares)
            return
        }

        const resultados = militares.filter(m => {
            switch (campo) {
                case 'matricula':
                    return normalize(m.matricula).includes(q)
                case 'nome':
                    return normalize(m.nomePaz).includes(q)
                default:
                    return false
            }
        })

        setMilitaresFiltrados(resultados)
    }, [militares])

    useEffect(() => {
        if (!ultimaPesquisa) {
            setMilitaresFiltrados(militares)
            return
        }
        gerenciarPesquisa(ultimaPesquisa)
    }, [militares, gerenciarPesquisa, ultimaPesquisa])

    const militaresTabela = militaresFiltrados ?? militares

    return (
        <div className={Styles.main}>
            <h2>Militares Escalaveis</h2>
            <div className={Styles.upload}>
                <label htmlFor="input_upload" className={Styles.label_upload}>Importe a Planilha dos Militares</label>
                <InputUpload
                    funcaoDownload={(signal) => MilitarClient.obterPlanilhaModeloMilitares(signal)}
                    funcaoUpload={(arquivo, signal) => MilitarClient.listarMilitaresEscalaveis(arquivo, signal)}
                    nomeModelo="modelo_militares.xlsx"
                    setDados={setMilitares}
                />
            </div>
            <BarraPesquisa
                campos={camposPesquisa}
                placeholder="Pesquisar militares..."
                pesquisar={gerenciarPesquisa}
            />
            <TabelaMilitares militaresTabela={militaresTabela} />
        </div>
    )
}
