import {DataGrid, GridActionsCellItem, GridRowModel, GridRowModesModel, GridRowsProp,} from "@mui/x-data-grid";
import {ReactElement, useEffect, useState} from "react";
import keycloak from "../../../keycloak/keycloak.ts";
import {handleRowModesModelChange} from "../../../functions/handlers.ts";
import useClientFetch from "../../../hooks/useClientFetch.ts";
import {deleteWarehouseUser, getWarehouseUsers} from "../../../client/warehouseUserClient.ts";
import {UserRoles} from "../../../model/UserRoles.ts";
import ToolbarWarehouseUsers from "./ToolbarWarehouseUsers.tsx";
import DeleteIcon from "@mui/icons-material/DeleteOutlined";

interface Props {
    warehouseId: number
}

export default ({warehouseId}: Props): ReactElement => {
    const [rows, setRows] = useState<GridRowsProp>([]);
    const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
    const [columns, setColumns] = useState<GridRowModel[]>([])

    const {
        data: warehouseUsersList, refetch
    } = useClientFetch(() => getWarehouseUsers(+warehouseId), []);

    useEffect(() => {
        if (warehouseUsersList) setRows(warehouseUsersList);
    }, [warehouseUsersList])

    useEffect(() => {
        setColumns([
            {
                field: 'fullName',
                headerName: 'Full name',
                width: 200,
                editable: true,
            },
            {
                field: 'username',
                headerName: 'Username',
                width: 200,
                editable: true,
            },
            {
                field: 'actions',
                type: 'actions',
                headerName: 'Actions',
                width: 200,
                cellClassName: 'actions',
                getActions: ({id}: {
                    id: string
                }) =>
                    ([<GridActionsCellItem
                        icon={<DeleteIcon/>}
                        label="Delete"
                        onClick={handleDeleteClick(id)}
                        color="inherit"
                    />]),
            }]
        )
    }, [])

    const handleDeleteClick = (id: string) => async () => {
        await deleteWarehouseUser(warehouseId, id);
        refetch();

        setRows(rows.filter((row) => row.id !== id));
    };

    useEffect(() => {
        keycloak.loadUserInfo().then(() => {
            // @ts-ignore
            const roles = keycloak?.userInfo?.roles?.includes(UserRoles.ROLE_ADMIN) || keycloak?.userInfo?.roles?.includes(UserRoles.ROLE_MANAGER)

            if (!roles) {
                setColumns([
                    {
                        field: 'fullName',
                        headerName: 'Full name',
                        width: 200,
                        editable: true,
                    },
                    {
                        field: 'username',
                        headerName: 'Username',
                        type: 'number',
                        width: 200,
                        editable: true,
                    },
                ])
            }
        })
    }, []);


    return (
        <div style={{height: 600, marginTop: '40px', paddingBottom: '100px'}}>
            <h3>Users</h3>
            <DataGrid
                rows={rows}
                columns={columns as any}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={(newModel) => handleRowModesModelChange(newModel, setRowModesModel)}
                slots={{
                    toolbar: ToolbarWarehouseUsers,
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


