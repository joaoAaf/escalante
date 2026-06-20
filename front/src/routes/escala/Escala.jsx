import Styles from './styles.module.css'
import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext'
import BarraPesquisa from '../../components/barra_pesquisa/BarraPesquisa'
import TabelaEscala from '../../components/tabela_escala/TabelaEscala'
import AcoesEscala from '../../components/acoes/AcoesEscala'
import EscalaClient from '../../clients/EscalaClient'
import InputUpload from '../../components/input_upload/InputUpload'
import FormCriarEscala from '../../components/form_criar_escala/FormCriarEscala'

export default function Escala() {

    const {escala} = useContext(GlobalContext)

    const [servicosAnteriores, setServicosAnteriores] = useState([])
    const [ultimaPesquisa, setUltimaPesquisa] = useState(null)

    const camposPesquisa = [
        {value: 'data', label: 'Data', inputType: 'date'},
        {value: 'matricula', label: 'Matrícula'},
        {value: 'militar', label: 'Militar Escalado'},
        {
            value: 'funcao',
            label: 'Função',
            options: [
                'Fiscal de Dia',
                'C.O.V.',
                'Operador de Linha 1',
                'Ajudante de Linha',
                'Permanente'
            ]
        }
    ]

    const gerenciarPesquisa = ({campo, consulta}) => {
        setUltimaPesquisa(pesquisa => {
            if (pesquisa && pesquisa.campo === campo && pesquisa.consulta === consulta) return pesquisa
            return {campo, consulta}
        })
    }

    return (
        <div className={Styles.main}>
            <h2>Escala de Serviço</h2>
            <div className={Styles.upload}>
                <label htmlFor="input_upload" className={Styles.label_upload}>Importe a Escala Anterior</label>
                <InputUpload
                    funcaoDownload={(token, signal) => EscalaClient.obterPlanilhaModeloEscala(token, signal)}
                    funcaoUpload={(arquivo, token, signal) => EscalaClient.importarEscalaXLSX(arquivo, token, signal)}
                    nomeModelo="modelo_escala.xlsx"
                    setDados={setServicosAnteriores}
                />
            </div>
            <FormCriarEscala servicosAnteriores={servicosAnteriores}/>
            <AcoesEscala/>
            <BarraPesquisa
                campos={camposPesquisa}
                placeholder="Pesquisar na escala..."
                pesquisar={gerenciarPesquisa}
            />
            <TabelaEscala escalaTabela={escala} busca={ultimaPesquisa}/>
        </div>
    )
}
