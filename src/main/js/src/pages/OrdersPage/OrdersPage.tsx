import * as React from 'react';
import {DataGrid, GridToolbar, GridRowsProp, GridRowParams} from '@mui/x-data-grid';
import {useNavigate} from "react-router-dom";
import routes from "../../routing/routes"

type Order = {
    id: number;
    createdBy: string;
    price: number;
    from: string;
    to: string;
};

const columns = [
    { field: 'id', headerName: 'ID', width: 90 },
    { field: 'createdBy', headerName: 'Created By', width: 150 },
    { field: 'price', headerName: 'Price (€)', width: 110 },
    { field: 'from', headerName: 'From', width: 130 },
    { field: 'to', headerName: 'To', width: 130 },
];

const OrdersPage = () => {
    const navigate = useNavigate();

    const orders: Order[] = [
        { id: 1, createdBy: 'User1', price: 100, from: 'Location A', to: 'Location B' },
        { id: 2, createdBy: 'User2', price: 150, from: 'Location C', to: 'Location D' },
        { id: 3, createdBy: 'User3', price: 255, from: 'Location A', to: 'Location B' },
        { id: 4, createdBy: 'User5', price: 577, from: 'Location C', to: 'Location A' },
        { id: 5, createdBy: 'User4', price: 7874, from: 'Location C', to: 'Location B' },
        { id: 6, createdBy: 'User1', price: 8285, from: 'Location B', to: 'Location C' },
        { id: 7, createdBy: 'User1', price: 100, from: 'Location A', to: 'Location B' },
        { id: 8, createdBy: 'User2', price: 150, from: 'Location C', to: 'Location D' },
        { id: 9, createdBy: 'User3', price: 255, from: 'Location A', to: 'Location B' },
        { id: 10, createdBy: 'User5', price: 577, from: 'Location C', to: 'Location A' },
        { id: 11, createdBy: 'User4', price: 7874, from: 'Location C', to: 'Location B' },
        { id: 12, createdBy: 'User1', price: 8285, from: 'Location B', to: 'Location C' },
        { id: 13, createdBy: 'User1', price: 100, from: 'Location A', to: 'Location B' },
        { id: 14, createdBy: 'User2', price: 150, from: 'Location C', to: 'Location D' },
        { id: 15, createdBy: 'User3', price: 255, from: 'Location A', to: 'Location B' },
        { id: 16, createdBy: 'User5', price: 577, from: 'Location C', to: 'Location A' },
        { id: 17, createdBy: 'User4', price: 7874, from: 'Location C', to: 'Location B' },
        { id: 18, createdBy: 'User1', price: 8285, from: 'Location B', to: 'Location C' },

    ];


    return (
        <div style={{ height: 400, width: '100%' }}>
            <h1>Orders</h1>
            <DataGrid
                slots={{ toolbar: GridToolbar }}
                rows={orders}
                columns={columns}
                rowHeight={35}
                headerHeight={40}
                autoPageSize
            />
        </div>
    );
};

export default OrdersPage;

