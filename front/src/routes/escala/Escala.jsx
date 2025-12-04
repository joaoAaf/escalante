import Styles from './styles.module.css'
import { useState, useEffect, useCallback, useContext } from 'react'
import { GlobalContext } from '../../context/GlobalContext'
import { CadastroServicoContextProvider } from '../../context/CadastroServicoContext'
import BarraPesquisa from '../../components/barra_pesquisa/BarraPesquisa'
import TabelaEscala from '../../components/tabela_escala/TabelaEscala'
import CadastroServico from '../../components/cadastro_servico/CadastroServico'
import AcoesEscala from '../../components/acoes_escala/AcoesEscala'
import EscalaClient from '../../clients/EscalaClient'
import InputUpload from '../../components/input_upload/InputUpload'
import FormCriarEscala from '../../components/form_criar_escala/FormCriarEscala'

export default function Escala() {

    const { escala } = useContext(GlobalContext)

    const [servicosAnteriores, setServicosAnteriores] = useState([])
    const [escalaFiltrada, setEscalaFiltrada] = useState(null)
    const [ultimaPesquisa, setUltimaPesquisa] = useState(null)

    const camposPesquisa = [
        { value: 'data', label: 'Data', inputType: 'date' },
        { value: 'matricula', label: 'Matrícula' },
        { value: 'militar', label: 'Militar Escalado' },
        {
            value: 'funcao',
            label: 'Função',
            options: [
                'Fiscal de Dia',
                'C.O.V.',
                'Operador de Linha',
                'Ajudante de Linha',
                'Permanente'
            ]
        }
    ]

    const normalize = v => String(v ?? '').toLowerCase()

    const gerenciarPesquisa = useCallback(({ campo, consulta }) => {
        setUltimaPesquisa(pesquisa => {
            if (pesquisa && pesquisa.campo === campo && pesquisa.consulta === consulta) return pesquisa
            return { campo, consulta }
        })
        const q = String(consulta ?? '').trim().toLowerCase()

        if (!escala) return

        if (campo === 'data') {
            if (!q) {
                setEscalaFiltrada(escala)
                return
            }
            const resultadosData = escala.filter(servico => {
                return (servico.dataServico === q)
            })
            setEscalaFiltrada(resultadosData)
            return
        }

        if (!q) {
            setEscalaFiltrada(escala)
            return
        }

        const resultados = escala.filter(servico => {
            switch (campo) {
                case 'matricula':
                    return normalize(servico.matricula ?? '').includes(q)
                case 'militar':
                    return normalize(servico.nomePaz ?? '').includes(q)
                case 'funcao':
                    return normalize(servico.funcao ?? '').includes(q)
                default:
                    return false
            }
        })

        setEscalaFiltrada(resultados)
    }, [escala, setEscalaFiltrada])

    useEffect(() => {
        if (!ultimaPesquisa) {
            setEscalaFiltrada(escala)
            return
        }
        gerenciarPesquisa(ultimaPesquisa)
    }, [escala, gerenciarPesquisa, ultimaPesquisa])

    const escalaTabela = escalaFiltrada ?? escala

    return (
        <div className={Styles.main}>
            <h2>Escala de Serviço</h2>
            <div className={Styles.upload}>
                <label htmlFor="input_upload" className={Styles.label_upload}>Importe a Escala Anterior</label>
                <InputUpload
                    funcaoDownload={(signal) => EscalaClient.obterPlanilhaModeloEscala(signal)}
                    funcaoUpload={(arquivo, signal) => EscalaClient.importarEscalaXLSX(arquivo, signal)}
                    nomeModelo="modelo_escala.xlsx"
                    setDados={setServicosAnteriores}
                />
            </div>
            <FormCriarEscala servicosAnteriores={servicosAnteriores} />
            <CadastroServicoContextProvider>
                <AcoesEscala />
                <CadastroServico />
            </CadastroServicoContextProvider>
            <BarraPesquisa
                campos={camposPesquisa}
                placeholder="Pesquisar na escala..."
                pesquisar={gerenciarPesquisa}
            />
            <TabelaEscala escalaTabela={escalaTabela} />
        </div>
    )
}
