import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import routes from './routing/routes'
import './App.scss'

const App = () => (
    <>
        <div>
            <a href="https://vitejs.dev" target="_blank">
                <img src={viteLogo} className="logo" alt="Vite logo"/>
            </a>
            <a href="https://react.dev" target="_blank">
                <img src={reactLogo} className="logo react" alt="React logo"/>
            </a>
        </div>
        <h1>Inventory system</h1>
        <div className="routes-list__wrapper">
            <h2>Routes</h2>
            <li>
                <a href={routes.intro}>IntroPage</a>
            </li>
            <li>
                <a href={routes.sign_in}>SignInPage</a>
            </li>
        </div>
    </>
)

export default App
