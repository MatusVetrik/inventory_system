import {DataGrid, GridRowModel, GridRowModesModel, GridRowsProp} from '@mui/x-data-grid';
import {useEffect, useState} from "react";
import {getListOrders} from "../../client/orderClient.ts";
import keycloak from "../../keycloak/keycloak.ts";
import {UserRoles} from "../../model/UserRoles.ts";
import {handleRowModesModelChange} from "../../functions/handlers.ts";
import ToolbarOrders from "./components/ToolbarOrders.tsx";
import useClientFetch from "../../hooks/useClientFetch.ts";

const initColumns = [
    {
        field: 'id',
        headerName: 'ID',
        flex: 0.5,
    },
    {
        field: 'createdByName',
        headerName: 'Created by',
        flex: 1,
    },
    {
        field: 'itemName',
        headerName: 'Item',
        flex: 1,
    },
    {
        field: 'quantity',
        headerName: 'Quantity',
        flex: 1,
    },
    {
        field: 'sourceName',
        headerName: 'Source',
        flex: 1,
    },
    {
        field: 'destinationName',
        headerName: 'Destination',
        flex: 1,
    },
]

const OrdersPage = () => {
    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
    const [columns, setColumns] = useState<GridRowModel[]>(initColumns)

    const {data: orderList, refetch} = useClientFetch(() => getListOrders(), []);

    useEffect(() => {
        if (orderList) setRows(orderList);
    }, [orderList])

    useEffect(() => {
        keycloak.loadUserInfo().then(() => {
            // @ts-ignore
            const roles = keycloak?.userInfo?.roles?.includes(UserRoles.ROLE_ADMIN) || keycloak?.userInfo?.roles?.includes(UserRoles.ROLE_MANAGER)

            if (!roles) {
                setColumns(initColumns)
            }
        })
    }, []);

    return (
        <div style={{height: 600, width: '80%', paddingBottom: '100px'}}>
            <h1>Orders</h1>
            <DataGrid
                rows={rows}
                columns={columns as any}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={(newModel) => handleRowModesModelChange(newModel, setRowModesModel)}
                slots={{
                    toolbar: ToolbarOrders,
                }}
                slotProps={{
                    toolbar: {refetch},
                }}
                rowHeight={35}
                autoPageSize
            />
        </div>
    );
};

export default OrdersPage;

