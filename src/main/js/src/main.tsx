import * as React from "react";
import {createRoot} from 'react-dom/client'
import './index.css'
import Router from "./routing/router";
import Layout from "./components/Layout";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <Layout>
            <Router/>
        </Layout>
    </React.StrictMode>,
)
