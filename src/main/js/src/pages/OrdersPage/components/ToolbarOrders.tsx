import {ChangeEvent, ReactElement, useState} from "react";
import {GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import CloseIcon from "@mui/icons-material/Close";
import {FormControl, InputLabel, MenuItem, Select, SelectChangeEvent, TextField} from "@mui/material";
import {UserRoles} from "../../../model/UserRoles.ts";
import PrivateComponent from "../../../components/PrivateComponent";
import {createOrder} from "../../../client/orderClient.ts";
import {Order} from "inventory-client-ts-axios";
import {getListWarehouses} from "../../../client/warehouseClient.ts";
import {getListWarehouseItems} from "../../../client/warehouseItemClient.ts";
import useClientFetch from "../../../hooks/useClientFetch.ts";
import {useToast} from "../../../components/Toast/Toast";

interface Props {
    refetch: () => void,
}

export default ({refetch}: Props): ReactElement => {

    const [visible, setVisible] = useState<boolean>(false);
    const [order, setOrder] = useState<Order>({} as Order);

    const [sourceWarehouseId, setSourceWarhouseId] = useState<number | null>(null);
    const {showToast} = useToast();

    const {data: warehouseList} = useClientFetch(() => getListWarehouses(), []);

    const {data: warehouseItemsList} = useClientFetch(
        () => getListWarehouseItems(sourceWarehouseId as number),
        [sourceWarehouseId as never]
    );

    const getItemQuantity = (): number => warehouseItemsList?.find(el => el?.id === order?.itemId)?.quantity ?? 0;

    const handleClick = async () => setVisible(!visible);

    const handleSubmit = async (): Promise<void> => {
        try {
            await createOrder(order);
            refetch();
            setVisible(false);
            setOrder({} as Order);
        } catch (error: any) {
            showToast(`${error.response.data.code} - ${error.response.data.message}`, {type: 'error'});
        }
    }

    const handleChange = (event: SelectChangeEvent<any> | ChangeEvent<any>, field: keyof Order) => {
        setOrder((prev: Order) => ({
            ...prev,
            [field]: +event.target.value
        }))
    };

    const isButtonDisabled = !(order.itemId && order.quantity && order.sourceId && order.destinationId);

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
                    alignItems: 'center',
                    margin: '5px',
                    width: '100%',
                    textAlign: 'left',
                }}>
                    <FormControl sx={{m: 1, flex: 1}}>
                        <InputLabel id="order-source__label">Source</InputLabel>
                        <Select
                            labelId="order-source__label"
                            id="order-source__id"
                            value={order?.sourceId ?? ''}
                            label="Source"
                            onChange={(e) => {
                                handleChange(e, 'sourceId');
                                setSourceWarhouseId(+e.target.value);
                            }}
                        >
                            {warehouseList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl sx={{m: 1, flex: 1}}>
                        <InputLabel id="order-item__label">Item</InputLabel>
                        <Select
                            labelId="order-item__label"
                            id="order-item__id"
                            value={order?.itemId ?? ''}
                            label="Item"
                            onChange={(e) => handleChange(e, 'itemId')}
                        >
                            {warehouseItemsList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.name} - {el.quantity}pc</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl sx={{m: 1, flex: 1, height: '100%'}}>
                        <TextField
                            id="order-quantity__id"
                            value={order?.quantity ?? ''}
                            onChange={(e) => handleChange(e, 'quantity')}
                            label="Quantity"
                            InputProps={{
                                inputProps: {
                                    max: getItemQuantity(),
                                    min: 0
                                }
                            }}
                            type="number"
                            variant="outlined"/>
                    </FormControl>
                    <FormControl sx={{m: 1, flex: 1}}>
                        <InputLabel id="order-destination__label">Destination</InputLabel>
                        <Select
                            labelId="order-destination__label"
                            id="order-destination__id"
                            value={order?.destinationId ?? ''}
                            label="Destination"
                            onChange={(e) => handleChange(e, 'destinationId')}
                            fullWidth
                        >
                            {warehouseList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <Button type="submit" onClick={handleSubmit} style={{maxHeight: "40px", marginLeft: '40px'}}
                            variant="contained" disabled={isButtonDisabled}>Submit</Button>
                </div>
            )
            }
        </GridToolbarContainer>
    );
}