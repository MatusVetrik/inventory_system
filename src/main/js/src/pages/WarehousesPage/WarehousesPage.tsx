import {DataGrid, GridRowModel, GridRowModes, GridRowModesModel, GridRowParams, GridRowsProp} from '@mui/x-data-grid';
import routes from "../../routing/routes";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {WarehouseUpdateRequest} from "inventory-client-ts-axios";
import {deleteWarehouse, getListWarehouses, updateWarehouse} from "../../client/warehouseClient.ts";
import EditToolbar from "./components/EditToolBarWarehouses.tsx";
import {handleRowEditStop, handleRowModesModelChange} from "../../functions/handlers.ts";
import GetActions from "../../components/GetActions/GetActions.tsx";
import useClientFetch from "../../hooks/useClientFetch.ts";


// const initialRows: GridRowsProp = [
//     {id: randomId(), capacity: 1235, name: "Bratislava"},
//     {id: randomId(), capacity: 326, name: "Lozorno"},
//     {id: randomId(), capacity: 1588, name: "Prague"},
//     {id: randomId(), capacity: 1236, name: "Vienna"},
//     {id: randomId(), capacity: 5668, name: "Budapest"},
//     {id: randomId(), capacity: 84989, name: "Brno"},
// ];

const WarehousePage = () => {
    const navigate = useNavigate();

    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});

    const {data: warehouseList, refetch} = useClientFetch(() => getListWarehouses(), []);

    useEffect(() => {
        if (warehouseList) setRows(warehouseList);
    }, [warehouseList])


    const handleDeleteClick = (id: number) => async () => {
        await deleteWarehouse(id);
        refetch();
        setRows(rows.filter((row) => row.id !== id));
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
        const newWarehouse: WarehouseUpdateRequest = {
            name: newRow?.name,
            capacity: +newRow?.capacity
        }
        await updateWarehouse(newRow?.id, newWarehouse);
        refetch();

        const updatedRow = {...newRow, isNew: false};
        setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
        return updatedRow;
    };

    function handleRowClick(row: GridRowParams<any>) {
        const rowData = rows.find(r => r.id === row.id);
        navigate(routes.warehouse, {state: {rowDetails: rowData}});
    }

    const columns = [
        {
            field: 'name',
            headerName: 'Name',
            width: 200,
            editable: true,
        },
        {
            field: 'capacity',
            headerName: 'Capacity',
            type: 'number',
            width: 200,
            editable: true,
        },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Actions',
            width: 200,
            cellClassName: 'actions',
            getActions: ({id}: { id: number }) => GetActions({
                id,
                rowModesModel,
                setRowModesModel,
                handleDeleteClick,
                handleCancelClick
            }),
        },
    ];

    return (
        <div style={{height: 600, width: '100%', textAlign: 'center'}}>
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
                onProcessRowUpdateError={(error) => console.log(error)}
                slots={{
                    toolbar: EditToolbar,
                }}
                slotProps={{
                    toolbar: {setRows, setRowModesModel, refetch},
                }}
                rowHeight={35}
                autoPageSize
            />
        </div>

    )
}
export default WarehousePage;