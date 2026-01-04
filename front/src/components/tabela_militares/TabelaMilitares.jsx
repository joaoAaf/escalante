import Styles from './styles.module.css'
import {useContext} from 'react'
import GlobalContext from '../../context/GlobalContext'
import BotaoRemover from '../botao_remover/BotaoRemover'
import {formatarData} from '../../utils/formatarData'
import AtualizacaoMilitar from "../atualizacao_form/AtualizacaoMilitar.jsx";

export default function TabelaMilitares({militaresTabela, tabela, setTabela, removerMilitar}) {

    const camposMilitar = {
        antiguidade: 'ANTIGUIDADE',
        matricula: 'MATRÍCULA',
        patente: 'POST./GRAD.',
        nomePaz: 'NOME DE PAZ',
        nascimento: 'NASCIMENTO',
        folgaEspecial: 'FOLGA ESPECIAL',
        cov: 'C.O.V.'
    }

    const ctx = useContext(GlobalContext)

    const sourceTabela = Array.isArray(tabela)
        ? tabela
        : Array.isArray(militaresTabela)
            ? militaresTabela
            : (Array.isArray(ctx?.militares) ? ctx.militares : [])

    const sourceSetTabela = setTabela ?? ctx?.setMilitares

    const criarCabecalho = () => (
        <tr>
            {Object.values(camposMilitar).map(campo => (
                <th key={campo}>{campo}</th>
            ))}
            <th>AÇÃO</th>
        </tr>
    )

    const listarMilitares = () => {
        const lista = sourceTabela

        return lista.length > 0 ? (
            lista.map((militar, index) => {
                const key = (militar?.matricula ?? '') || `m-${index}`
                return (
                    <tr key={key}>
                        {listarDadosMilitar(militar)}
                        <td>
                            <AtualizacaoMilitar
                                matricula={militar?.matricula}
                                matriculaKey={'matricula'}
                            />
                            {' '}
                            <BotaoRemover
                                id={militar?.matricula}
                                idKey={'matricula'}
                                tabela={lista}
                                setTabela={sourceSetTabela}
                                campos={Object.values(camposMilitar)}
                                apiRemover={removerMilitar}
                            />
                        </td>
                    </tr>
                )
            })
        ) : (
            <tr>
                <td colSpan="8">Nenhum militar encontrado. Importe ou cadastre os militares</td>
            </tr>
        )
    }

    const listarDadosMilitar = militar => {
        const m = {...militar, nascimento: formatarData(militar.nascimento)}
        return Object.keys(camposMilitar).map((campo) => {
            if (campo === 'cov') return <td key={campo}>{checkboxCov(militar)}</td>
            return <td key={campo}>{m[campo] ?? "-"}</td>
        })
    }

    const checkboxCov = militar => (
        <input
            type="checkbox"
            checked={militar.cov === true}
            onChange={() => alterarCov(militar.matricula)}
        />
    )

    const alterarCov = matricula => {
        if (typeof sourceSetTabela !== 'function') return
        sourceSetTabela(prev => (prev || []).map(militar => {
            if (militar.matricula === matricula)
                return {...militar, cov: !militar.cov}
            return militar
        }))
    }

    return (
        <table className={Styles.table}>
            <thead>
            {criarCabecalho()}
            </thead>
            <tbody>
            {listarMilitares()}
            </tbody>
        </table>
    )
}
