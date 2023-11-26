import {ChangeEvent, ReactElement, useState} from "react";
import {GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import CloseIcon from "@mui/icons-material/Close";
import {FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, TextField} from "@mui/material";
import {UserRoles} from "../../../model/UserRoles.ts";
import PrivateComponent from "../../../components/PrivateComponent";
import useClientFetch from "../../../hooks/useClientFetch.ts";
import {createOrder} from "../../../client/orderClient.ts";
import {Order} from "inventory-client-ts-axios";
import {getListWarehouses} from "../../../client/warehouseClient.ts";
import {getListWarehouseItems} from "../../../client/warehouseItemClient.ts";

interface Props {
    refetch: () => void,
}

export default ({refetch}: Props): ReactElement => {

    const [visible, setVisible] = useState<boolean>(false);
    const [order, setOrder] = useState<Order>(null);
    const [sourceWarehouseId, setSourceWarhouseId] = useState<number>(0);

    const {data: warehouseList} = useClientFetch(() => getListWarehouses(), []);

    const {data: warehouseItemsList} = useClientFetch(() => getListWarehouseItems(sourceWarehouseId), [sourceWarehouseId]);

    const handleClick = async () => setVisible(!visible);

    const handleSubmit = async (): Promise<void> => {
        console.log(order)
        await createOrder();
        refetch();
        setVisible(false);
        setOrder(null);
    }

    const handleChange = (event: SelectChangeEvent | ChangeEvent<any>, field: keyof Order) => {
        setOrder((prev: Order) => ({
            ...prev,
            [field]: +event.target.value
        }))
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
                <PrivateComponent allowedRoles={[UserRoles.ROLE_ADMIN, UserRoles.ROLE_MANAGER]}>

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
                                Add order
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
                    <FormControl sx={{m: 1, minWidth: 150}}>
                        <InputLabel id="order-source__label">Source</InputLabel>
                        <Select
                            labelId="order-source__label"
                            id="order-source__id"
                            value={order?.sourceWarehouse}
                            label="User"
                            onChange={(e) => {
                                handleChange(e, 'sourceWarehouse');
                                setSourceWarhouseId(e.target.value);
                            }}
                        >
                            {warehouseList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl sx={{m: 1, minWidth: 150}}>
                        <InputLabel id="order-item__label">Item</InputLabel>
                        <Select
                            labelId="order-item__label"
                            id="order-item__id"
                            value={order?.itemId}
                            label="User"
                            onChange={(e) => handleChange(e, 'itemId')}
                        >
                            {warehouseItemsList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.name} - {el.quantity}pc</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl sx={{m: 1, minWidth: 100}}>
                        <TextField
                            id="order-quantity__id"
                            value={order?.quantity}
                            onChange={(e) => handleChange(e, 'quantity')}
                            label="Quantity"
                            type="number"
                            variant="outlined" size="small"/>
                    </FormControl>
                    <FormControl sx={{m: 1, minWidth: 150}}>
                        <InputLabel id="order-destination__label">Destination</InputLabel>
                        <Select
                            labelId="order-destination__label"
                            id="order-destination__id"
                            value={order?.destinationId}
                            label="User"
                            onChange={(e) => handleChange(e, 'destinationId')}
                        >
                            {warehouseList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <Button type="submit" onClick={handleSubmit} style={{maxHeight: "40px"}}
                            variant="contained">Submit</Button>
                </div>
            )
            }
        </GridToolbarContainer>
    );
}