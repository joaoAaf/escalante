import { useContext, useState } from 'react'
import Modal from '../modal/Modal'
import BotoesModal from '../modal/BotoesModal'
import Styles from './styles.module.css'
import UsuarioClient from '../../clients/UsuarioClient'
import { GlobalContext } from '../../context/GlobalContext'

export default function CadastroUsuario({ abrir, fechar, setUsuarios }) {
  const { token, setFeedback } = useContext(GlobalContext)
  const [email, setEmail] = useState('')
  const [perfilSelect, setPerfilSelect] = useState('')
  const [perfis, setPerfis] = useState([])

  const adicionarPerfil = () => {
    if (!perfilSelect) return setFeedback({ type: 'info', mensagem: 'Selecione um perfil para adicionar.' })
    if (perfis.includes(perfilSelect)) return setFeedback({ type: 'info', mensagem: 'Perfil já adicionado.' })
    setPerfis([...perfis, perfilSelect])
    setPerfilSelect('')
  }

  const removerPerfil = perfil => setPerfis(perfis.filter(p => p !== perfil))

  const cadastrar = async evento => {
    evento.preventDefault()
    const form = evento.currentTarget

    if (!form.checkValidity()) {
      for (const element of form.elements) {
        if (element.willValidate && !element.checkValidity()) {
          return setFeedback({ type: 'info', mensagem: element.validationMessage })
        }
      }
    }

    if (!perfis || perfis.length === 0) {
      return setFeedback({ type: 'info', mensagem: 'Adicione pelo menos um perfil ao usuário.' })
    }

    try {
      const controller = new AbortController()
      const usuarioRequest = { username: email, perfis }
      const criado = await UsuarioClient.cadastrarUsuario(token, usuarioRequest, controller.signal)
      // Se o backend não retornar o objeto criado, usamos os dados do request
      setUsuarios(prev => [...(prev || []), criado || usuarioRequest])
      setEmail('')
      setPerfis([])
      setPerfilSelect('')
      fechar()
    } catch (error) {
      if (error.name === 'AbortError') return
      setFeedback({ type: 'error', mensagem: error.message })
    }
  }

  return (
    <Modal abrir={abrir} fechar={fechar} titulo="Cadastrar Usuário">
      <form onSubmit={cadastrar} className={Styles.CadastroUsuario} noValidate>

        <label>Email:</label>
        <input
          type="email"
          value={email}
          onChange={e => { e.target.setCustomValidity(''); setEmail(e.target.value) }}
          required
          maxLength={130}
          pattern={"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"}
          onInvalid={e => e.target.setCustomValidity('Por favor, digite um email válido.')}
        />

          <label>Perfis selecionados:</label>

          <div className={Styles.perfilSelect}>
          <select
            value={perfilSelect}
            onChange={e => { e.target.setCustomValidity(''); setPerfilSelect(e.target.value) }}
          >
            <option value="" disabled>Selecione o Perfil</option>
            <option>ADMIN</option>
            <option>ESCALANTE</option>
          </select>
          <button type="button" onClick={adicionarPerfil}>Adicionar</button>
        </div>

          <div className={Styles.perfis}>
              {perfis.map(p => (
                  <span key={p}>
                        {p} <button type="button" className={Styles.clearBtn} onClick={() => removerPerfil(p)}>x</button>
                    </span>))}
          </div>
        <input type="text" readOnly value={perfis.join(', ')} hidden/>

        <BotoesModal
          typeConfirmar="submit"
          cancelar={fechar}
        />

      </form>
    </Modal>
  )
}

