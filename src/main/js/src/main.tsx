import { createRoot } from 'react-dom/client';
import Router from "./routing/router";
import Layout from "./components/Layout";
import { BrowserRouter } from "react-router-dom";
import { ReactKeycloakProvider } from '@react-keycloak/web';

import keycloak from "./keycloak/keycloak.ts";
import Loading from "./components/Loading";
import {ToastProvider} from "./components/Toast/Toast";

createRoot(document.getElementById('root')!).render(
    <ReactKeycloakProvider authClient={keycloak}
                           initOptions={{ onLoad: 'login-required', pkceMethod: "S256" }}
                           autoRefreshToken={false}
                           LoadingComponent={<Loading />}>
      <BrowserRouter>
        <ToastProvider>
          <Layout>
            <Router/>
          </Layout>
        </ToastProvider>
      </BrowserRouter>
    </ReactKeycloakProvider>,
);
