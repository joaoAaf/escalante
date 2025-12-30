import {useContext, useRef, useState} from 'react'
import {GlobalContext} from '../../context/GlobalContext'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'
import Edit from './assets/edit.png'
import Styles from './styles.module.css'
import InputPerfis from "../input_perfis/InputPerfis.jsx";
import UsuarioClient from "../../clients/UsuarioClient.js";

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

        if (perfis.length > usuario?.perfis.length) {

            const usuarioAtualizado = {...usuario, perfis: perfis.filter(p => !usuario.perfis.includes(p))}

            UsuarioClient.adicionarPerfis(token, usuarioAtualizado, controller.signal)
                .then(perfis => usuario.perfis = perfis)
                .catch(error => {
                    if (error.name === 'AbortError') return
                    setFeedback({type: 'error', mensagem: error.message})
                })
                .catch(error => {
                    if (error.name === 'AbortError') return
                    setFeedback({type: 'error', mensagem: error.message})
                })
                .finally(() => {
                    if (abortControllerRef.current === controller)
                        abortControllerRef.current = null
                })
        } else {

            const usuarioAtualizado = {...usuario, perfis: usuario.perfis.filter(p => !perfis.includes(p))}

            UsuarioClient.removerPerfis(token, usuarioAtualizado, controller.signal)
                .then(perfis => usuario.perfis = perfis)
                .catch(error => {
                    if (error.name === 'AbortError') return
                    setFeedback({type: 'error', mensagem: error.message})
                })
                .finally(() => {
                    if (abortControllerRef.current === controller)
                        abortControllerRef.current = null
                })
        }

        const usuariosAtualizados = usuarios.map(item => String(item?.[idKey]) === String(id) ? usuario : item)
        setUsuarios(usuariosAtualizados)
        setFeedback({type: 'success', mensagem: 'Perfis atualizados com sucesso.'})
    }

    return (
        <>
            <a
                href="#"
                onClick={() => setStatusModal(true)}
                className={Styles.linkAtualizacao}
            >
                <img src={Edit} alt="Atualizar Perfis"/>
            </a>
            <Modal abrir={statusModal} fechar={() => setStatusModal(false)} titulo="Confirmar Remoção">
                <div className={Styles.formPerfis}>
                    <InputPerfis perfis={perfis} setPerfis={setPerfis}/>
                    <BotoesModal confirmar={atualizarPerfis} cancelar={() => setStatusModal(false)}/>
                </div>
            </Modal>

        </>
    )

}
