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
                <a href={routes.sign_in}>SignInPage</a>
            </li>
        </div>
    </>
)

export default App
