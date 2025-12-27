import Styles from './styles.module.css'
import FireFighter from './assets/firefighter.png'
import Notes from './assets/notes.png'
import Users from './assets/users.png'
import { NavLink } from 'react-router-dom'

export default function Sidebar() {
    return (
        <div className={Styles.sidebar}>
            <h1>Escalante</h1>
            <nav>
                <NavLink
                    to="/"
                    title="Militares"
                    className={({ isActive }) => isActive ? Styles.active : '' }
                >
                    <img src={FireFighter} alt="Militares" />
                    <span>Militares</span>
                </NavLink>
                <NavLink
                    to="/escala"
                    title="Escala"
                    className={({ isActive }) => isActive ? Styles.active : '' }
                >
                    <img src={Notes} alt="Escala" />
                    <span>Escala</span>
                </NavLink>
                <NavLink
                    to="/usuarios"
                    title="Usuários"
                    className={({ isActive }) => isActive ? Styles.active : '' }
                >
                    <img src={Users} alt="Usuários" />
                    <span>Usuários</span>
                </NavLink>
            </nav>
        </div>
    )
}
