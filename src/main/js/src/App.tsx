import './App.scss'
import routes from "./routing/routes";

const App = () => (
    <>
        <div className="routes-list__wrapper">
            <h2>Routes</h2>
            <li>
                <a href={routes.intro}>IntroPage</a>
            </li>
            <li>
            </li>
        </div>
    </>
)

export default App
