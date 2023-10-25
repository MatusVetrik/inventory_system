import * as React from 'react';
import {useNavigate} from "react-router-dom";
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ListItemText from '@mui/material/ListItemText';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import routes from "../../../routing/routes";

export default () => {
    const navigate = useNavigate();

    return (
        <React.Fragment>
            <ListItemButton onClick={() => navigate(routes.intro)}>
                <ListItemIcon>
                    <DashboardIcon/>
                </ListItemIcon>
                <ListItemText primary="Dashboard"/>
            </ListItemButton>
            <ListItemButton onClick={() => navigate(routes.orders)}>
                <ListItemIcon>
                    <ShoppingCartIcon/>
                </ListItemIcon>
                <ListItemText primary="Orders"/>
            </ListItemButton>
            {/*<ListItemButton>*/}
            {/*    <ListItemIcon>*/}
            {/*        <PeopleIcon/>*/}
            {/*    </ListItemIcon>*/}
            {/*    <ListItemText primary="Customers"/>*/}
            {/*</ListItemButton>*/}
            <ListItemButton onClick={() => navigate(routes.warehouses)}>
                <ListItemIcon>
                    <WarehouseIcon/>
                </ListItemIcon>
                <ListItemText primary="Warehouses"/>
            </ListItemButton>
            {/*<ListItemButton>*/}
            {/*    <ListItemIcon>*/}
            {/*        <InventoryIcon/>*/}
            {/*    </ListItemIcon>*/}
            {/*    <ListItemText primary="Inventory"/>*/}
            {/*</ListItemButton>*/}
        </React.Fragment>
    )
};