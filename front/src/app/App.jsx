import './App.css'
import Sidebar from '../components/sidebar/Sidebar'
import {Outlet} from 'react-router-dom'
import {GlobalContextProvider} from '../context/GlobalContext'
import FeedbackToast from '../components/feedback_toast/FeedbackToast'

export default function App() {
  return (
    <div className="container">
      <Sidebar />
      <div className='main-content'>
        <GlobalContextProvider>
          <Outlet />
          <FeedbackToast />
        </GlobalContextProvider>
      </div>
    </div>
  )
}
