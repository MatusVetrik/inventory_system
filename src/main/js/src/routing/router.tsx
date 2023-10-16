import React, { ReactElement } from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import routes from './routes';
import App from "../App";
import IntroPage from "../pages/IntroPage";

const Router = (): ReactElement => (
    <BrowserRouter>
        <Routes>
            <Route path={routes.index} element={<App />} />
            <Route path={routes.intro} element={<IntroPage />} />
        </Routes>
    </BrowserRouter>
);

export default Router;
