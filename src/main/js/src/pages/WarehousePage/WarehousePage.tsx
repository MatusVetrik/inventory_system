import {useLocation} from "react-router-dom";
import {
    DataGrid,
    GridActionsCellItem,
    GridEventListener,
    GridRowEditStopReasons,
    GridRowModel,
    GridRowModes,
    GridRowModesModel,
    GridRowsProp,
} from "@mui/x-data-grid";
import SaveIcon from "@mui/icons-material/Save";
import CancelIcon from "@mui/icons-material/Close";
import EditIcon from "@mui/icons-material/Edit";
import {ReactElement, useState} from "react";
import DeleteIcon from "@mui/icons-material/DeleteOutlined";
import EditToolbar from "./components/EditToolbar.tsx";
import {randomId} from "@mui/x-data-grid-generator";
import {deleteWarehouseItem, updateWarehouseItem} from "../../client/warehouseItemClient.ts";
import {WarehouseItemRequest} from "inventory-client-ts-axios";


const WarehousePage = (): ReactElement => {
    const location = useLocation();
    const {id: warehouseId, name: warehouseName, capacity: warehouseCapacity} = location.state?.rowDetails;

    const initialRows: GridRowsProp = [
        {id: randomId(), name: 'Item1', size: 100, quantity: 100},
        {id: randomId(), name: 'Item2', size: 150, quantity: 200},
        {id: randomId(), name: 'Item3', size: 120, quantity: 180},
        {id: randomId(), name: 'Item6', size: 90, quantity: 110},
        {id: randomId(), name: 'Item9', size: 70, quantity: 80},
        {id: randomId(), name: 'Item10', size: 180, quantity: 220},
        {id: randomId(), name: 'Item14', size: 75, quantity: 95},
        {id: randomId(), name: 'Item15', size: 165, quantity: 210},
        {id: randomId(), name: 'Item20', size: 155, quantity: 200}
    ];

    const [rows, setRows] = useState(initialRows);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});

    const handleRowEditStop: GridEventListener<'rowEditStop'> = (params, event) => {
        if (params.reason === GridRowEditStopReasons.rowFocusOut) {
            event.defaultMuiPrevented = true;
        }
    };
    const handleEditClick = (id: number) => () => {
        setRowModesModel({...rowModesModel, [id]: {mode: GridRowModes.Edit}});
    };

    const handleSaveClick = (id: number) => () => {
        setRowModesModel({...rowModesModel, [id]: {mode: GridRowModes.View}});
    };

    const handleDeleteClick = (id: number) => async () => {
        await deleteWarehouseItem(warehouseId, id);
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

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        setRowModesModel(newRowModesModel);
    };

    const processRowUpdate = async (newRow: GridRowModel) => {
        const newItem: WarehouseItemRequest = {
            name: newRow?.name,
            size: +newRow?.size,
            quantity: +newRow?.quantity
        }
        await updateWarehouseItem(warehouseId, newRow?.id, newItem);

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
            getActions: ({id}: { id: number }) => {
                const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

                if (isInEditMode) {
                    return [
                        <GridActionsCellItem
                            icon={<SaveIcon/>}
                            label="Save"
                            sx={{
                                color: 'primary.main',
                            }}
                            onClick={handleSaveClick(id)}
                        />,
                        <GridActionsCellItem
                            icon={<CancelIcon/>}
                            label="Cancel"
                            className="textPrimary"
                            onClick={handleCancelClick(id)}
                            color="inherit"
                        />,
                    ];
                }

                return [
                    <GridActionsCellItem
                        icon={<EditIcon/>}
                        label="Edit"
                        className="textPrimary"
                        onClick={handleEditClick(id)}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        icon={<DeleteIcon/>}
                        label="Delete"
                        onClick={handleDeleteClick(id)}
                        color="inherit"
                    />,
                ];
            },
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
                onRowModesModelChange={handleRowModesModelChange}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={(newRow) => processRowUpdate(newRow)}
                onProcessRowUpdateError={(error) => console.log(error)}
                slots={{
                    toolbar: EditToolbar,
                }}
                slotProps={{
                    toolbar: {setRows, setRowModesModel, warehouseId},
                }}
                rowHeight={35}
                autoPageSize
            />
        </div>
    );

};

export default WarehousePage;

