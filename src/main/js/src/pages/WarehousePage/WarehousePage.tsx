import {useParams} from "react-router-dom";
import {
    DataGrid,
    GridColDef,
    GridRowModel,
    GridRowModes,
    GridRowModesModel,
    GridRowsProp,
    GridValidRowModel,
} from "@mui/x-data-grid";
import {ReactElement, useEffect, useState} from "react";
import ToolbarWarehouseItems from "./components/ToolbarWarehouseItems.tsx";
import {deleteWarehouseItem, getListWarehouseItems, updateWarehouseItem} from "../../client/warehouseItemClient.ts";
import {WarehouseItemRequest} from "inventory-client-ts-axios";
import {handleRowEditStop, handleRowModesModelChange} from "../../functions/handlers.ts";
import GetActions from "../../components/GetActions/GetActions.tsx";
import useClientFetch from "../../hooks/useClientFetch.ts";
import {getWarehouseById} from "../../client/warehouseClient.ts";
import {CircularProgress} from "@mui/material";
import Users from "./users/Users.tsx";
import {useToast} from "../../components/Toast/Toast";
import keycloak from "../../keycloak/keycloak.ts";
import {UserRoles} from "../../model/UserRoles.ts";

const WarehousePage = (): ReactElement => {
    const {warehouseId} = useParams<{
        warehouseId: string
    }>();

    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
    const {showToast} = useToast();
    const {data: warehouse} = useClientFetch(() => getWarehouseById(+warehouseId!), []);
    const [allowed, setIsAllowed] = useState<boolean>(false);
    const {
        data: warehouseItemsList,
        refetch
    } = useClientFetch(() => getListWarehouseItems(+warehouseId!), []);

    useEffect(() => {
        if (warehouseItemsList) setRows(warehouseItemsList);
    }, [warehouseItemsList])

    const handleDeleteClick = (id: number) => async () => {
        try {
            await deleteWarehouseItem(+warehouseId!, id);
            refetch();
            setRows(rows.filter((row) => row.id !== id));
        } catch (error: any) {
            showToast(`${error.response.data.code} - ${error.response.data.message}`, {type: 'error'});
        }
    };

    const handleCancelClick = (id: number) => () => {
        setRowModesModel({
            ...rowModesModel,
            [id]: {mode: GridRowModes.View, ignoreModifications: true},
        });

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow!.isNew) {
            setRows(rows.filter((row) => row.id !== id));
        }
    };

    const processRowUpdate = async (newRow: GridRowModel) => {
        try {
            const newItem: WarehouseItemRequest = {
                name: newRow?.name,
                size: +newRow?.size,
                quantity: +newRow?.quantity
            };
            await updateWarehouseItem(+warehouseId!, newRow?.id, newItem);
            refetch();

            const updatedRow = {...newRow, isNew: false};
            setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
            return updatedRow;
        } catch (error: any) {
            showToast(`${error.response.data.code} - ${error.response.data.message}`, {type: 'error'});
            throw error;
        }
    };

    useEffect(() => {
        keycloak.loadUserInfo().then(() => {
            const roles = (keycloak?.userInfo as any)?.roles?.includes(UserRoles.ROLE_ADMIN) || (keycloak?.userInfo as any)?.roles?.includes(UserRoles.ROLE_MANAGER)
            setIsAllowed(!!roles)
        })
    }, []);


    const columns = [
        {
            field: 'name',
            headerName: 'Name',
            flex: 1,
            headerAlign: 'left',
            align: 'left',
            editable: true,
        },
        {
            field: 'size',
            headerName: 'Size',
            type: 'number',
            flex: 1,
            headerAlign: 'left',
            align: 'left',
            editable: true,
        },
        {
            field: 'quantity',
            headerName: 'Quantity',
            type: 'number',
            flex: 1,
            headerAlign: 'left',
            align: 'left',
            editable: true,
        },
        allowed && {
            field: 'actions',
            type: 'actions',
            headerName: 'Actions',
            headerAlign: 'right',
            align: 'right',
            flex: 1,
            cellClassName: 'actions',
            getActions: ({id}: {
                id: number
            }) => GetActions({
                id,
                rowModesModel,
                setRowModesModel,
                handleDeleteClick,
                handleCancelClick,
            }),
        }] as GridColDef<GridValidRowModel>[]

    return (
        <div style={{height: 600, width: '80%', textAlign: 'center'}}>
            {
                warehouse ?
                    <>
                        <h1>{warehouse?.name} warehouse</h1>
                        <div style={{display: 'flex', justifyContent: 'center', gap: '40px'}}>
                            <h2>Capacity: {warehouse?.capacity}</h2>
                            <h2>Filled: {warehouse?.itemsCapacitySize}</h2>
                        </div>
                        <h3>Items</h3>
                    </> :
                    <>
                        <h1><CircularProgress/></h1>
                        <h2><CircularProgress/></h2>
                        <h3><CircularProgress/></h3>
                    </>
            }
            <DataGrid
                rows={rows}
                columns={columns}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={(newModel) => handleRowModesModelChange(newModel, setRowModesModel)}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={(newRow) => processRowUpdate(newRow)}
                slots={{
                    toolbar: ToolbarWarehouseItems,
                }}
                slotProps={{
                    toolbar: {warehouseId, refetch},
                }}
                rowHeight={35}
                autoPageSize
            />
            <Users warehouseId={+warehouseId!}/>
        </div>

    );

};

export default WarehousePage;

