import {useLocation} from "react-router-dom";
import {DataGrid, GridRowModel, GridRowModes, GridRowModesModel, GridRowsProp,} from "@mui/x-data-grid";
import {ReactElement, useEffect, useState} from "react";
import EditToolbar from "./components/EditToolbarWarehouseItems.tsx";
import {deleteWarehouseItem, getListWarehouseItems, updateWarehouseItem} from "../../client/warehouseItemClient.ts";
import {WarehouseItemRequest} from "inventory-client-ts-axios";
import {handleRowEditStop, handleRowModesModelChange} from "../../functions/handlers.ts";
import GetActions from "../../components/GetActions/GetActions.tsx";
import useClientFetch from "../../hooks/useClientFetch.ts";

// const initialRows: GridRowsProp = [
//     {id: randomId(), name: 'Item1', size: 100, quantity: 100},
//     {id: randomId(), name: 'Item2', size: 150, quantity: 200},
//     {id: randomId(), name: 'Item3', size: 120, quantity: 180},
//     {id: randomId(), name: 'Item6', size: 90, quantity: 110},
//     {id: randomId(), name: 'Item9', size: 70, quantity: 80},
//     {id: randomId(), name: 'Item10', size: 180, quantity: 220},
//     {id: randomId(), name: 'Item14', size: 75, quantity: 95},
//     {id: randomId(), name: 'Item15', size: 165, quantity: 210},
//     {id: randomId(), name: 'Item20', size: 155, quantity: 200}
// ];

const WarehousePage = (): ReactElement => {
    const location = useLocation();
    const {id: warehouseId, name: warehouseName, capacity: warehouseCapacity} = location.state?.rowDetails;

    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});

    const {data: warehouseItemsList, refetch} = useClientFetch(() => getListWarehouseItems(warehouseId), []);

    useEffect(() => {
        if (warehouseItemsList) setRows(warehouseItemsList);
    }, [warehouseItemsList])

    const handleDeleteClick = (id: number) => async () => {
        await deleteWarehouseItem(warehouseId, id);
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
        const newItem: WarehouseItemRequest = {
            name: newRow?.name,
            size: +newRow?.size,
            quantity: +newRow?.quantity
        }
        await updateWarehouseItem(warehouseId, newRow?.id, newItem);
        refetch();

        const updatedRow = {...newRow, isNew: false};
        setRows(rows.map((row) => (row.id === newRow.id ? updatedRow : row)));
        return updatedRow;
    };

    const columns = [
        {
            field: 'name',
            headerName: 'Name',
            width: 200,
            editable: true,
        },
        {
            field: 'size',
            headerName: 'Size',
            type: 'number',
            width: 200,
            editable: true,
        },
        {
            field: 'quantity',
            headerName: 'Quantity',
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
            <h1>{warehouseName} warehouse</h1>
            <h2>Capacity: {warehouseCapacity}</h2>
            <h3>Items for warehouse {warehouseName}</h3>
            <DataGrid
                rows={rows}
                columns={columns}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={(newModel) => handleRowModesModelChange(newModel, setRowModesModel)}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={(newRow) => processRowUpdate(newRow)}
                onProcessRowUpdateError={(error) => console.log(error)}
                slots={{
                    toolbar: EditToolbar,
                }}
                slotProps={{
                    toolbar: {warehouseId, refetch},
                }}
                rowHeight={35}
                autoPageSize
            />
        </div>
    );

};

export default WarehousePage;

