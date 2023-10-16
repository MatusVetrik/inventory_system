import * as React from "react";
import { createRoot} from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import Router from "./routing/router";

createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
      <Router/>
  </React.StrictMode>,
)
