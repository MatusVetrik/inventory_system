import * as React from 'react';

import {useLocation, useNavigate} from "react-router-dom";
import {DataGrid, GridToolbar} from "@mui/x-data-grid";
import {Button} from "@mui/material";
import routes from "../../routing/routes";
import {WarehouseItem} from "inventory-client-ts-axios";

interface Item {
    id: number,
    name: string,
    size: number,
    quantity: number,
}

const columns = [
    {field: 'id', headerName: 'ID', width: 100},
    {field: 'name', headerName: 'Name', width: 200},
    {field: 'size', headerName: 'Size', width: 200},
    {field: 'quantity', headerName: 'Quantity', width: 200},
];

const WarehousePage = (props) => {
    const location = useLocation();
    const rowDetails = location.state?.rowDetails;
    const navigate = useNavigate();

    const items: WarehouseItem[] = [
        {id: 1, name: 'Item1', size: 100, quantity: 100},
        {id: 2, name: 'Item2', size: 150, quantity: 200},
        {id: 3, name: 'Item3', size: 120, quantity: 180},
        {id: 6, name: 'Item6', size: 90, quantity: 110},
        {id: 9, name: 'Item9', size: 70, quantity: 80},
        {id: 10, name: 'Item10', size: 180, quantity: 220},
        {id: 14, name: 'Item14', size: 75, quantity: 95},
        {id: 15, name: 'Item15', size: 165, quantity: 210},
        {id: 20, name: 'Item20', size: 155, quantity: 200}
    ];

    const onClickAddItem = (): void => {
        navigate(routes.items, {state: {rowDetails}});
    }


    return (
        <div style={{height: 400, width: '100%', textAlign: 'center'}}>
            <h1>{rowDetails.location} warehouse</h1>
            <h2>Capacity: {rowDetails.capacity} </h2>
            <div style={{
                width: '100%',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
            }}>
                <h3>Items for warehouse no.{rowDetails.id}</h3>
                <Button variant="contained" onClick={onClickAddItem} style={{height: "40px"}}>Add item</Button>
            </div>
            <DataGrid
                slots={{toolbar: GridToolbar}}
                rows={items}
                columns={columns}
                rowHeight={35}
                headerHeight={40}
                autoPageSize
            />
        </div>
    );

};

export default WarehousePage;

