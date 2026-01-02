import './App.css'
import Sidebar from '../components/sidebar/Sidebar'
import {Outlet} from 'react-router-dom'

export default function App() {
    return (
        <div className="container">
            <Sidebar/>
            <div className='main-content'>
                <Outlet/>
            </div>
        </div>
    )
}
