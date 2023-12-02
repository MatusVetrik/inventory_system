import {
    DataGrid,
    GridColDef,
    GridRowModel,
    GridRowModes,
    GridRowModesModel,
    GridRowParams,
    GridRowsProp,
    GridValidRowModel,
} from '@mui/x-data-grid';
import routes from "../../routing/routes";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {WarehouseUpdateRequest} from "inventory-client-ts-axios";
import {deleteWarehouse, getListWarehouses, updateWarehouse} from "../../client/warehouseClient.ts";
import ToolBarWarehouseList from "./components/ToolbarWarehouseList.tsx";
import {handleRowEditStop, handleRowModesModelChange} from "../../functions/handlers.ts";
import GetActions from "../../components/GetActions/GetActions.tsx";
import useClientFetch from "../../hooks/useClientFetch.ts";
import keycloak from "../../keycloak/keycloak";
import {useToast} from "../../components/Toast/Toast";
import {UserRoles} from "../../model/UserRoles.ts";

const WarehousePage = () => {
    const navigate = useNavigate();

    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
    const [allowed, setIsAllowed] = useState<boolean>(false);
    const {data: warehouseList, refetch} = useClientFetch(() => getListWarehouses(), []);
    useEffect(() => {
        if (warehouseList) setRows(warehouseList);
    }, [warehouseList])
    const {showToast} = useToast();

    const handleDeleteClick = (id: number) => async () => {
        try {
            await deleteWarehouse(id);
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
            const newWarehouse: WarehouseUpdateRequest = {
                name: newRow?.name,
                capacity: +newRow?.capacity
            };
            await updateWarehouse(newRow?.id, newWarehouse);
            refetch();

            const updatedRow = {...newRow, isNew: false};
            setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
            return updatedRow;
        } catch (error: any) {
            showToast(`${error.response.data.code} - ${error.response.data.message}`, {type: 'error'});
        }
    };

    function handleRowClick(row: GridRowParams<any>) {
        const rowData = rows.find(r => r.id === row.id);
        navigate(routes.warehouse.detail.withId(+row.id), {state: {rowDetails: rowData}});
    }


    useEffect(() => {
        keycloak.loadUserInfo().then(() => {
            const roles = (keycloak?.userInfo as any)?.roles?.includes(UserRoles.ROLE_ADMIN)
            setIsAllowed(!!roles);
        })
    }, []);

    const columns = [
        {
            field: 'name',
            headerName: 'Name',
            headerAlign: 'left',
            align: 'left',
            flex: 1,
            editable: true,
        },
        {
            field: 'capacity',
            headerName: 'Capacity',
            type: 'number',
            headerAlign: 'left',
            align: 'left',
            flex: 1,
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
            <h1>Warehouses</h1>
            <DataGrid
                rows={rows}
                columns={columns}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowClick={(row) => handleRowClick(row)}
                onRowModesModelChange={(newModel) => handleRowModesModelChange(newModel, setRowModesModel)}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={(newRow) => processRowUpdate(newRow)}
                slots={{
                    toolbar: ToolBarWarehouseList,
                }}
                slotProps={{
                    toolbar: {refetch},
                }}
                rowHeight={35}
                autoPageSize
            />
        </div>

    )
}
export default WarehousePage;