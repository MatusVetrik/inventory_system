import * as React from "react";
import {createRoot} from 'react-dom/client'
import './index.css'
import Router from "./routing/router";
import Layout from "./components/Layout";
import {BrowserRouter} from "react-router-dom";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <BrowserRouter>
            <Layout>
                <Router/>
            </Layout>
        </BrowserRouter>
    </React.StrictMode>,
)
