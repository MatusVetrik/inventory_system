import {ReactElement} from 'react';
import {Route, Routes} from 'react-router-dom';
import routes from './routes';
import App from "../App";
import IntroPage from "../pages/IntroPage/IntroPage";
import OrdersPage from "../pages/OrdersPage/OrdersPage";
import ProfilePage from "../pages/ProfilePage/ProfilePage";
import WarehousesPage from "../pages/WarehousesPage/WarehousesPage";

const Router = (): ReactElement => (
    <Routes>
        <Route path={routes.index} element={<App/>}/>
        <Route path={routes.intro} element={<IntroPage/>}/>
        <Route path={routes.orders} element={<OrdersPage/>}/>
        <Route path={routes.profile} element={<ProfilePage/>}/>
        <Route path={routes.warehouses} element={<WarehousesPage/>}/>
    </Routes>
);

export default Router;
