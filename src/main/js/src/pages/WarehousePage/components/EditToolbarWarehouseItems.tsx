import {ReactElement, useState} from "react";
import {GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import {createWarehouseItem} from "../../../client/warehouseItemClient.ts";
import {WarehouseItemRequest} from "inventory-client-ts-axios";
import CloseIcon from "@mui/icons-material/Close";
import {TextField} from "@mui/material";
import {UserRoles} from "../../../model/UserRoles";
import PrivateComponent from "../../../components/PrivateComponent";

interface Props {
    warehouseId: number,
    refetch: () => void,
}

export default ({warehouseId, refetch}: Props): ReactElement => {

    const [visible, setVisible] = useState<boolean>(false);
    const [newItem, setNewItem] = useState<WarehouseItemRequest>({name: "", quantity: 0, size: 0});

    const handleClick = async () => setVisible(!visible);

    const handleSubmit = async (): Promise<void> => {
        await createWarehouseItem(warehouseId, newItem);
        refetch();
        setVisible(false);
        setNewItem({name: "", quantity: 0, size: 0});
    }

    const setWarehouseAttribute = (attr: 'name' | 'size' | 'quantity', value: string | number) => setNewItem(prev => ({
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
                <PrivateComponent allowedRoles={[UserRoles.ROLE_ADMIN,UserRoles.ROLE_MANAGER]} >

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
                            Add item
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
                            value={newItem?.name}
                            onChange={e => setWarehouseAttribute("name", e.target.value)}
                            label="Name"
                            variant="outlined"
                            size="small"/>
                        <TextField
                            id="warehouse-capacity__id"
                            value={newItem?.size}
                            onChange={e => setWarehouseAttribute("size", e.target.value)}
                            label="Size"
                            type="number"
                            variant="outlined" size="small"/>
                        <TextField
                            id="warehouse-capacity__id"
                            value={newItem?.quantity}
                            onChange={e => setWarehouseAttribute("quantity", e.target.value)}
                            label="Quantity"
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