import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './app/App'
import Militares from './routes/militares/Militares'
import Escala from './routes/escala/Escala'
import Login from './routes/login/Login'
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import {GlobalContextProvider} from './context/GlobalContext'

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { path: '/', element: <Militares /> },
      { path: '/escala', element: <Escala /> },
    ],
  },
  { path: '/login', element: <GlobalContextProvider><Login /></GlobalContextProvider> },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
