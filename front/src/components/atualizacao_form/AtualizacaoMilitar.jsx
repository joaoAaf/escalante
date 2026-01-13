import {useContext, useState} from 'react'
import GlobalContext from '../../context/GlobalContext.jsx'
import Modal from '../modal/Modal.jsx'
import FormMilitar from "../form_modal/FormMilitar.jsx";
import LinkAtualizacao from "../link_atualizacao/LinkAtualizacao.jsx";

export default function AtualizacaoMilitar({militares, setMilitares, matricula, matriculaKey, apiAtualizar}) {

    const {setFeedback} = useContext(GlobalContext)
    const [statusModal, setStatusModal] = useState(false)

    const militarExistente = militares.find(item => item?.[matriculaKey] === String(matricula))

    const [militar, setMilitar] = useState(militarExistente)

    const atualizarMilitar = async evento => {
        evento.preventDefault()

        const form = evento.currentTarget
        if (!form.checkValidity()) {
            for (const element of form.elements) {
                if (element.willValidate && !element.checkValidity()) {
                    return setFeedback({type: 'info', mensagem: element.validationMessage})
                }
            }
        }

        if (!militar?.antiguidade || militar?.antiguidade === 0) setMilitar({...militar, antiguidade: null})
        if (!militar?.folgaEspecial) setMilitar({...militar, folgaEspecial: 0})

        if (apiAtualizar) {
            try {
                await apiAtualizar(militar)
                setStatusModal(false)
                setFeedback({type: 'success', mensagem: 'Militar atualizado com sucesso.'})
                return
            } catch (error) {
                if (error.name === 'AbortError') return
                setFeedback({type: 'error', mensagem: error.message})
                return
            }
        }

        const militaresAtualizados = (militares || []).filter(item => String(item?.[matriculaKey]) !== String(matricula))
        setMilitares([...militaresAtualizados, militar].sort((a, b) => a.antiguidade - b.antiguidade))
        setStatusModal(false)
        setFeedback({type: 'success', mensagem: 'Militar atualizado com sucesso.'})
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
