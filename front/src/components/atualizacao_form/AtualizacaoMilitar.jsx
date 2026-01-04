import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext.jsx'
import Modal from '../modal/Modal.jsx'
import FormMilitar from "../form_modal/FormMilitar.jsx";
import LinkAtualizacao from "../link_atualizacao/LinkAtualizacao.jsx";

export default function AtualizacaoMilitar({matricula, matriculaKey}) {

    const {militares, setMilitares, setFeedback} = useContext(GlobalContext)
    const [statusModal, setStatusModal] = useState(false)

    const militarExistente = militares.find(item => item?.[matriculaKey] === String(matricula))

    const [militar, setMilitar] = useState(militarExistente)

    const atualizarMilitar = evento => {
        evento.preventDefault()

        const form = evento.currentTarget
        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        const militarAtualizado = {...militar}
        militarAtualizado.antiguidade = militarAtualizado.antiguidade ? Number(militarAtualizado.antiguidade) : undefined
        militarAtualizado.folgaEspecial = militarAtualizado.folgaEspecial ? Number(militarAtualizado.folgaEspecial) : 0

        setMilitares(prev => [...(prev || []), militarAtualizado])
        setMilitar(militarExistente)
        setStatusModal(false)
        setFeedback({type: 'success', mensagem: 'Militar cadastrado com sucesso.'})
    }

    return (
        <>
            <LinkAtualizacao setStatusModal={setStatusModal} altLink="Atualizar Militar"/>
            <Modal abrir={statusModal} fechar={() => setStatusModal(false)} titulo="Atualizar Militar">
                <FormMilitar
                    militar={militar}
                    setMilitar={setMilitar}
                    fechar={() => setStatusModal(false)}
                    acao={atualizarMilitar}
                />
            </Modal>
        </>
    )
}
