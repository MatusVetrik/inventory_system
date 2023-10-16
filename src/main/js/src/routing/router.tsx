import React, {ReactElement} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import routes from './routes';
import App from "../App";
import IntroPage from "../pages/IntroPage";
import SignInPage from "../pages/SignInPage";

const Router = (): ReactElement => (
    <BrowserRouter>
        <Routes>
            <Route path={routes.index} element={<App/>}/>
            <Route path={routes.intro} element={<IntroPage/>}/>
            <Route path={routes.sign_in} element={<SignInPage/>}/>
        </Routes>
    </BrowserRouter>
);

export default Router;
