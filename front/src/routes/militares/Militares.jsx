import Styles from './styles.module.css'
import {useCallback, useContext, useEffect, useRef, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import BarraPesquisa from '../../components/barra_pesquisa/BarraPesquisa'
import InputUpload from '../../components/input_upload/InputUpload'
import TabelaMilitares from '../../components/tabela_militares/TabelaMilitares'
import MilitarClient from '../../clients/MilitarClient'
import CadastroListaMilitares from '../../components/cadastro_militares/CadastroListaMilitares.jsx'
import AcoesMilitares from "../../components/acoes/AcoesMilitares.jsx";

export default function Militares() {

    const {militares, setMilitares, token, setFeedback, reload, setReload} = useContext(GlobalContext)

    const [militaresFiltrados, setMilitaresFiltrados] = useState(null)
    const [ultimaPesquisa, setUltimaPesquisa] = useState(null)
    const [carregando, setCarregando] = useState(false)
    const [abrirModalImportacao, setAbrirModalImportacao] = useState(false)
    const [militaresImportados, setMilitaresImportados] = useState(null)

    const abortControllerRef = useRef(null)

    const camposPesquisa = [
        {value: 'nome', label: 'Nome de Paz'},
        {value: 'matricula', label: 'Matrícula'},
        {value: 'cov', label: 'C.O.V.', disableInput: true}
    ]

    const normalize = v => String(v ?? '').toLowerCase()

    const carregarMilitares = useCallback(() => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller

        if (!token) {
            setMilitares([])
            return
        }

        setCarregando(true)
        MilitarClient.listarMilitares(token, controller.signal)
            .then(lista => {
                setMilitares(Array.isArray(lista) ? lista : [])
                setReload(false)
            })
            .catch(error => {
                if (error.name === 'AbortError') return
                setMilitares([])
                setFeedback?.({type: 'error', mensagem: error.message})
            })
            .finally(() => {
                setCarregando(false)
                if (abortControllerRef.current === controller)
                    abortControllerRef.current = null
            })
    }, [token, setMilitares, setReload, setFeedback])

    useEffect(() => {
        if (reload) carregarMilitares()
        return () => abortControllerRef.current?.abort()
    }, [carregarMilitares, reload])

    const gerenciarPesquisa = useCallback(({campo, consulta}) => {
        setUltimaPesquisa(pesquisa => {
            if (pesquisa && pesquisa.campo === campo && pesquisa.consulta === consulta) return pesquisa
            return {campo, consulta}
        })

        const q = String(consulta ?? '').trim().toLowerCase()

        if (!Array.isArray(militares)) return

        if (campo === 'cov') {
            setMilitaresFiltrados(militares.filter(m => m.cov === true))
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

    const iniciarRevisaoImportacao = listaImportada => {
        if (!Array.isArray(listaImportada) || listaImportada.length === 0) {
            setFeedback?.({type: 'info', mensagem: 'A planilha não retornou militares para revisão.'})
            return
        }

        setMilitaresImportados(listaImportada)
        setAbrirModalImportacao(true)
    }

    const cancelarImportacao = () => {
        setAbrirModalImportacao(false)
        setMilitaresImportados(null)
    }

    const sucessoCadastro = () => {
        cancelarImportacao()
        carregarMilitares()
    }

    return (
        <div className={Styles.main}>
            <h2>Militares Escalaveis</h2>

            <div className={Styles.upload}>
                <label htmlFor="input_upload" className={Styles.label_upload}>Importe a Planilha dos Militares</label>
                <InputUpload
                    funcaoDownload={(t, signal) => MilitarClient.obterPlanilhaModeloMilitares(t, signal)}
                    funcaoUpload={(arquivo, t, signal) => MilitarClient.importarMilitaresXLSX(arquivo, t, signal)}
                    nomeModelo="modelo_militares.xlsx"
                    setDados={iniciarRevisaoImportacao}
                />
            </div>

            <AcoesMilitares/>

            <BarraPesquisa
                campos={camposPesquisa}
                placeholder="Pesquisar militares..."
                pesquisar={gerenciarPesquisa}
            />

            {carregando && <p>Carregando militares...</p>}
            <TabelaMilitares militaresTabela={militaresTabela}/>

            <CadastroListaMilitares
                abrir={abrirModalImportacao}
                fechar={cancelarImportacao}
                militaresImportados={militaresImportados}
                onSucesso={sucessoCadastro}
            />
        </div>
    )
}
