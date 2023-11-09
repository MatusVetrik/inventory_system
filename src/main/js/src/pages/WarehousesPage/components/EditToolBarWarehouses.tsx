import {ReactElement} from "react";
import {GridRowModes, GridRowModesModel, GridRowsProp, GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import {randomId} from "@mui/x-data-grid-generator";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import {WarehouseRequest} from "inventory-client-ts-axios";
import {createWarehouse} from "../../../client/warehouseClient.ts";

interface Props {
    setRows: (newRows: (oldRows: GridRowsProp) => GridRowsProp) => void;
    setRowModesModel: (
        newModel: (oldModel: GridRowModesModel) => GridRowModesModel,
    ) => void;
    warehouseId: number,
    refetch: () => void
}

export default ({setRows, setRowModesModel, refetch}: Props): ReactElement => {
    const handleClick = async () => {
        const newWarehouse: WarehouseRequest = {
            name: "",
            capacity: 0,
            userId: ""
        }
        await createWarehouse(newWarehouse);

        const id = randomId();
        setRows((oldRows) => [...oldRows, {
            id,
            name: newWarehouse.name,
            capacity: newWarehouse.capacity,
            isNew: true
        }]);
        setRowModesModel((oldModel) => ({
            ...oldModel,
            [id]: {mode: GridRowModes.Edit, fieldToFocus: 'name'},
        }));

        refetch();
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
                    Add warehouse
                </Button>
            </div>
        </GridToolbarContainer>
    );
}
