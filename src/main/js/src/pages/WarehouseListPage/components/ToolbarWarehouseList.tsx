import {ReactElement, useState} from "react";
import {GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import CloseIcon from '@mui/icons-material/Close';
import {TextField} from "@mui/material";
import {WarehouseRequest} from "inventory-client-ts-axios";
import {createWarehouse} from "../../../client/warehouseClient.ts";
import PrivateComponent from "../../../components/PrivateComponent";
import {UserRoles} from "../../../model/UserRoles";
import {useToast} from "../../../components/Toast/Toast";

interface Props {
    refetch: () => void
}

export default ({refetch}: Props): ReactElement => {
    const [visible, setVisible] = useState<boolean>(false);
    const [newWarehouse, setNewWarehouse] = useState<WarehouseRequest>({capacity: 0, name: ""});
    const { showToast } = useToast();

    const handleClick = async () => setVisible(!visible);

    const handleSubmit = async (): Promise<void> => {
        try {
            await createWarehouse(newWarehouse);
            refetch();
            setVisible(false);
            setNewWarehouse({capacity: 0, name: ""});
        } catch (error) {
            showToast(`Error: ${error.message}`, { type: 'error' });
        }
    }

    const setWarehouseAttribute = (attr: 'name' | 'capacity', value: string | number) => setNewWarehouse(prev => ({
        ...prev,
        [attr]: value
    }))

    return (
        <GridToolbarContainer>
            <div style={{
                width: '100%',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
            }}>
                <GridToolbar style={{paddingTop: "10px"}}/>
                <PrivateComponent allowedRoles={[UserRoles.ROLE_ADMIN]}>
                {
                    visible ?
                        <Button
                            variant="contained"
                            color="error"
                            startIcon={<CloseIcon/>}
                            style={{height: "30px"}}
                            onClick={handleClick}>
                            Cancel
                        </Button>
                        : <Button
                            variant="contained"
                            color="primary"
                            startIcon={<AddIcon/>}
                            style={{height: "30px"}}
                            onClick={handleClick}>
                            Add warehouse
                        </Button>

                }
                </PrivateComponent>
            </div>
            {visible && (
                <div style={{
                    display: 'flex',
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    margin: '5px',
                    width: '100%',
                }}>
                    <div style={{
                        display: 'flex',
                        gap: '10px',
                    }}>
                        <TextField
                            id="warehouse-name__id"
                            value={newWarehouse?.name}
                            onChange={e => setWarehouseAttribute("name", e.target.value)}
                            label="Name"
                            variant="outlined"
                            size="small"/>
                        <TextField
                            id="warehouse-capacity__id"
                            value={newWarehouse?.capacity}
                            onChange={e => setWarehouseAttribute("capacity", e.target.value)}
                            label="Capacity"
                            type="number"
                            variant="outlined" size="small"/>
                    </div>
                    <Button type="submit" onClick={handleSubmit} variant="contained">Submit</Button>
                </div>
            )
            }
        </GridToolbarContainer>
    );
}
