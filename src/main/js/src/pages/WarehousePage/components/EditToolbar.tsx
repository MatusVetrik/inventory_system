import {ReactElement} from "react";
import {GridRowModes, GridRowModesModel, GridRowsProp, GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import {randomId} from "@mui/x-data-grid-generator";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import {createWarehouseItem} from "../../../client/warehouseItemClient.ts";
import {WarehouseItemRequest} from "inventory-client-ts-axios";

interface Props {
    setRows: (newRows: (oldRows: GridRowsProp) => GridRowsProp) => void;
    setRowModesModel: (
        newModel: (oldModel: GridRowModesModel) => GridRowModesModel,
    ) => void;
    warehouseId: number,
}

export default ({setRows, setRowModesModel, warehouseId}: Props): ReactElement => {
    const handleClick = async () => {
        const newItem: WarehouseItemRequest = {
            name: "",
            quantity: 0,
            size: 0,
        }
        await createWarehouseItem(warehouseId, newItem);

        const id = randomId();
        setRows((oldRows) => [...oldRows, {id, name: '', age: '', isNew: true}]);
        setRowModesModel((oldModel) => ({
            ...oldModel,
            [id]: {mode: GridRowModes.Edit, fieldToFocus: 'name'},
        }));
    };

    return (
        <GridToolbarContainer>
            <div style={{
                width: '100%',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
            }}>
                <GridToolbar style={{paddingTop: "10px"}}/>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon/>}
                    style={{height: "30px"}}
                    onClick={handleClick}>
                    Add record
                </Button>
            </div>
        </GridToolbarContainer>
    );
}