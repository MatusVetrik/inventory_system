import {ReactElement, useState} from "react";
import {GridToolbar, GridToolbarContainer} from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import CloseIcon from "@mui/icons-material/Close";
import {FormControl, InputLabel, MenuItem, Select, SelectChangeEvent} from "@mui/material";
import {UserRoles} from "../../../model/UserRoles.ts";
import PrivateComponent from "../../../components/PrivateComponent";
import {addWarehouseUser} from "../../../client/warehouseUserClient.ts";
import useClientFetch from "../../../hooks/useClientFetch.ts";
import {getListUsers} from "../../../client/userClient.ts";

interface Props {
    warehouseId: number,
    refetch: () => void,
}

export default ({warehouseId, refetch}: Props): ReactElement => {

    const [visible, setVisible] = useState<boolean>(false);
    const [userId, setUserId] = useState<string>("");

    const {data: usersList} = useClientFetch(() => getListUsers(), []);

    const handleClick = async () => setVisible(!visible);

    const handleSubmit = async (): Promise<void> => {
        await addWarehouseUser(warehouseId, userId);
        refetch();
        setVisible(false);
        setUserId("");
    }

    const handleChange = (event: SelectChangeEvent) => {
        setUserId(event.target.value);
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
                                Add user
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
                    <FormControl sx={{m: 1, minWidth: 200}}>
                        <InputLabel id="user-select__label">User</InputLabel>
                        <Select
                            labelId="user-select__label"
                            id="user-select__id"
                            value={userId}
                            label="User"
                            onChange={handleChange}
                        >
                            {usersList?.map(el => (
                                <MenuItem key={el.id} value={el.id}>{el.fullName}</MenuItem>
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