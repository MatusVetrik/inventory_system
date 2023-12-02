import {ReactElement} from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';
import routes from './routes';
import OrdersPage from "../pages/OrdersPage/OrdersPage";
import ProfilePage from "../pages/ProfilePage/ProfilePage";
import WarehousesPage from "../pages/WarehouseListPage/WarehouseListPage.tsx";
import WarehousePage from "../pages/WarehousePage/WarehousePage";
import PageNotFound from "../pages/PageNotFound/PageNotFound.tsx";

const Router = (): ReactElement => (
    <Routes>
        <Route path={routes.index} element={<Navigate to="/app/profile" replace={true}/>}/>
        <Route path={routes.app} element={<Navigate to="/app/profile" replace={true}/>}/>
        <Route path={routes.orders} element={<OrdersPage/>}/>
        <Route path={routes.profile} element={<ProfilePage/>}/>
        <Route path={routes.warehouse.list} element={<WarehousesPage/>}/>
        <Route path={routes.warehouse.detail.raw} element={<WarehousePage/>}/>
        <Route path={routes.pageNotFound} element={<PageNotFound/>}/>
        <Route path="*" element={<Navigate to="/app/page-not-found"/>}/>
    </Routes>
);

export default Router;
