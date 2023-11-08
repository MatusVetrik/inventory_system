import * as React from 'react';
import {useState} from 'react';

import {useLocation} from "react-router-dom";
import {DataGrid, GridEventListener, GridToolbar} from "@mui/x-data-grid";
import {Button, Dialog, DialogTitle} from "@mui/material";

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

const ItemsPage = (props) => {
    const location = useLocation();
    const rowDetails = location.state?.rowDetails;
    const [open, setOpen] = useState<boolean>(false);
    const [selectedItem, setSelectedItem] = useState<any>();

    const items: Item[] = [
        {id: 1, name: 'Item1', size: 100, quantity: 100},
        {id: 2, name: 'Item2', size: 150, quantity: 200},
        {id: 3, name: 'Item3', size: 120, quantity: 180},
        {id: 4, name: 'Item4', size: 80, quantity: 90},
        {id: 5, name: 'Item5', size: 200, quantity: 250},
        {id: 6, name: 'Item6', size: 90, quantity: 110},
        {id: 7, name: 'Item7', size: 130, quantity: 160},
        {id: 8, name: 'Item8', size: 110, quantity: 140},
        {id: 9, name: 'Item9', size: 70, quantity: 80},
        {id: 10, name: 'Item10', size: 180, quantity: 220},
        {id: 11, name: 'Item11', size: 95, quantity: 120},
        {id: 12, name: 'Item12', size: 135, quantity: 170},
        {id: 13, name: 'Item13', size: 115, quantity: 130},
        {id: 14, name: 'Item14', size: 75, quantity: 95},
        {id: 15, name: 'Item15', size: 165, quantity: 210},
        {id: 16, name: 'Item16', size: 85, quantity: 100},
        {id: 17, name: 'Item17', size: 145, quantity: 190},
        {id: 18, name: 'Item18', size: 125, quantity: 150},
        {id: 19, name: 'Item19', size: 65, quantity: 70},
        {id: 20, name: 'Item20', size: 155, quantity: 200}
    ];

    const onClickRow: GridEventListener<'rowClick'> = (
        params, // GridRowParams
        event, // MuiEvent<React.MouseEvent<HTMLElement>>
        details, // GridCallbackDetails
    ) => {
        setOpen(open);
        setSelectedItem(params.row);
    };

    const onClickAddItem = (): void => {
        console.log(selectedItem);
    }

    const handleDialogClose = (): void => setOpen(false);


    return (
        <div style={{height: 400, width: '100%', textAlign: 'center'}}>
            <h1>Items</h1>
            <DataGrid
                slots={{toolbar: GridToolbar}}
                rows={items}
                columns={columns}
                rowHeight={35}
                headerHeight={40}
                onRowClick={onClickRow}
                autoPageSize
            />
            <Dialog onClose={handleDialogClose} open={open}>
                <DialogTitle>Do you want add this item to warehouse?</DialogTitle>
                <Button onClick={onClickAddItem}>Add</Button>
            </Dialog>
        </div>
    );

};

export default ItemsPage;

