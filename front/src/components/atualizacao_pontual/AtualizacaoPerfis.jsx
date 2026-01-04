import {useContext, useRef, useState} from 'react'
import GlobalContext from '../../context/GlobalContext.jsx'
import Modal from '../modal/Modal.jsx'
import BotoesModal from '../modal/BotoesModal.jsx'
import Styles from './styles.module.css'
import InputPerfis from "../input_perfis/InputPerfis.jsx";
import UsuarioClient from "../../clients/UsuarioClient.js";
import LinkAtualizacao from "../link_atualizacao/LinkAtualizacao.jsx";

export default function AtualizacaoPerfis({usuarios, setUsuarios, id, idKey}) {

    const usuario = usuarios.find(item => String(item?.[idKey]) === String(id))

    const {token, setFeedback} = useContext(GlobalContext)
    const [statusModal, setStatusModal] = useState(false)
    const [perfis, setPerfis] = useState(usuario?.perfis || [])

    const abortControllerRef = useRef(null)

    const criarAbortController = () => {
        abortControllerRef.current?.abort()
        const controller = new AbortController()
        abortControllerRef.current = controller
        return controller
    }

    const atualizarPerfis = async () => {
        setStatusModal(false)

        if (!usuario || !perfis || perfis.length === 0) {
            return setFeedback({type: 'info', mensagem: 'O usuário deve possuir ao menos um perfil.'})
        }

        if (perfis.length === usuario?.perfis.length) {
            return setFeedback({type: 'info', mensagem: 'Nenhuma alteração nos perfis para atualizar.'})
        }

        const controller = criarAbortController()

        try {
            let perfisAtualizados = usuario.perfis

            if (perfis.length > usuario?.perfis.length) {
                const usuarioAtualizado = {...usuario, perfis: perfis.filter(p => !usuario.perfis.includes(p))}
                perfisAtualizados = await UsuarioClient.adicionarPerfis(token, usuarioAtualizado, controller.signal)
            } else {
                const usuarioAtualizado = {...usuario, perfis: usuario.perfis.filter(p => !perfis.includes(p))}
                perfisAtualizados = await UsuarioClient.removerPerfis(token, usuarioAtualizado, controller.signal)
            }

            const usuariosAtualizados =
                usuarios.map(item => String(item?.[idKey]) === String(id) ? {
                    ...item, perfis: perfisAtualizados
                } : item)
            setUsuarios(usuariosAtualizados)
            setFeedback({type: 'success', mensagem: 'Perfis atualizados com sucesso.'})

        } catch (error) {
            if (error.name === 'AbortError') return
            setFeedback({type: 'error', mensagem: error.message})
        } finally {
            if (abortControllerRef.current === controller)
                abortControllerRef.current = null
        }
    }

    return (
        <>
            <LinkAtualizacao setStatusModal={setStatusModal} altLink="Atualizar Perfis"/>
            <Modal abrir={statusModal} fechar={() => setStatusModal(false)} titulo="Atualizar Perfis">
                <div className={Styles.form}>
                    <InputPerfis perfis={perfis} setPerfis={setPerfis}/>
                    <BotoesModal confirmar={atualizarPerfis} cancelar={() => setStatusModal(false)}/>
                </div>
            </Modal>

        </>
    )

}
