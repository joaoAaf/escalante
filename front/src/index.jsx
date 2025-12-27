import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './app/App'
import Militares from './routes/militares/Militares'
import Escala from './routes/escala/Escala'
import Login from './routes/login/Login'
import Usuarios from './routes/usuarios/Usuarios'
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import {GlobalContextProvider} from './context/GlobalContext'
import FeedbackToast from './components/feedback_toast/FeedbackToast'
import AtualizacaoSenha from "./routes/alteracao-senha/AtualizacaoSenha.jsx";

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { path: '/', element: <Militares /> },
      { path: '/escala', element: <Escala /> },
      { path: '/usuarios', element: <Usuarios /> },
    ],
  },
  { path: '/login', element: <Login /> },
  { path: '/usuarios/password', element: <AtualizacaoSenha /> },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <GlobalContextProvider>
      <RouterProvider router={router} />
      <FeedbackToast />
    </GlobalContextProvider>
  </StrictMode>,
)
