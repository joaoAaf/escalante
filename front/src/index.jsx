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
import AtualizacaoSenha from "./routes/atualizacao-senha/AtualizacaoSenha.jsx"
import RotasGateway from './components/route/RotasGateway.jsx'
import RotasProtegidas from './components/route/RotasProtegidas.jsx'

const router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {path: '/', element: <RotasGateway/>},
            {
                path: '/militares',
                element: <RotasProtegidas perfisPermitidos={["ESCALANTE"]}><Militares/></RotasProtegidas>
            },
            {path: '/escala', element: <RotasProtegidas perfisPermitidos={["ESCALANTE"]}><Escala/></RotasProtegidas>},
            {path: '/usuarios', element: <RotasProtegidas perfisPermitidos={["ADMIN"]}><Usuarios/></RotasProtegidas>},
        ],
    },
    {path: '/login', element: <Login/>},
    {path: '/password', element: <AtualizacaoSenha/>},
    {path: '*', element: <RotasGateway/>},
]);

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <GlobalContextProvider>
            <RouterProvider router={router}/>
            <FeedbackToast/>
        </GlobalContextProvider>
    </StrictMode>,
)
