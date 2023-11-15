import {ReactElement} from 'react';
import {Route, Routes} from 'react-router-dom';
import routes from './routes';
import App from "../App";
import OrdersPage from "../pages/OrdersPage/OrdersPage";
import ProfilePage from "../pages/ProfilePage/ProfilePage";
import WarehousesPage from "../pages/WarehouseListPage/WarehouseListPage.tsx";
import WarehousePage from "../pages/WarehousePage/WarehousePage";

const Router = (): ReactElement => (
    <Routes>
        <Route path={routes.index} element={<App/>}/>
        <Route path={routes.orders} element={<OrdersPage/>}/>
        <Route path={routes.profile} element={<ProfilePage/>}/>
        <Route path={routes.warehouse.list} element={<WarehousesPage/>}/>
        <Route path={routes.warehouse.detail.raw} element={<WarehousePage/>}/>
    </Routes>
);

export default Router;
