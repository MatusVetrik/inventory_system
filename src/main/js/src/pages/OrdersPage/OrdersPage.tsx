import {
  DataGrid,
  GridActionsCellItem,
  GridRowModel,
  GridRowModesModel,
  GridRowsProp
} from '@mui/x-data-grid';
import {useEffect, useState} from "react";
import useClientFetch from "../../hooks/useClientFetch.ts";
import {deleteOrder, getListOrders} from "../../client/orderClient.ts";
import DeleteIcon from "@mui/icons-material/DeleteOutlined";
import keycloak from "../../keycloak/keycloak.ts";
import {UserRoles} from "../../model/UserRoles.ts";
import {handleRowModesModelChange} from "../../functions/handlers.ts";
import ToolbarOrders from "./components/ToolbarOrders.tsx";
import {ToastProvider, useToast} from "../../components/Toast/Toast";

const initColumns = [
  {
    field: 'id',
    headerName: 'ID',
    width: 200,
    editable: true,
  },
  {
    field: 'createdBy',
    headerName: 'Created by',
    width: 200,
    editable: true,
  },
  {
    field: 'itemId',
    headerName: 'Item ID',
    width: 200,
    editable: true,
  },
  {
    field: 'quantity',
    headerName: 'Quantity',
    width: 200,
    editable: true,
  },
  {
    field: 'sourceId',
    headerName: 'Source ID',
    width: 200,
    editable: true,
  },
  {
    field: 'destinationId',
    headerName: 'Destination ID',
    width: 200,
    editable: true,
  },
]

const OrdersPage = () => {
  const [rows, setRows] = useState<GridRowsProp>([]);
  const [rowModesModel, setRowModesModel] = useState<GridRowModesModel>({});
  const [columns, setColumns] = useState<GridRowModel[]>([])

  const {data: orderList, refetch} = useClientFetch(() => getListOrders(), []);

  useEffect(() => {
    if (orderList) setRows(orderList);
  }, [orderList])

  useEffect(() => {
    setColumns([
      ...initColumns,
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

  const { showToast } = useToast();

  const handleDeleteClick = (id: string) => async () => {
    try {
      await deleteOrder(); // Ensure you pass the 'id' to the deleteOrder function
      refetch();
      setRows(rows.filter((row) => row.id !== id));
    } catch (error) {
      showToast(`Error: ${error.message}`, { type: 'error' });
    }
  };

  useEffect(() => {
    keycloak.loadUserInfo().then(() => {
      // @ts-ignore
      const roles = keycloak?.userInfo?.roles?.includes(UserRoles.ROLE_ADMIN) || keycloak?.userInfo?.roles?.includes(UserRoles.ROLE_MANAGER)

      if (!roles) {
        setColumns(initColumns)
      }
    })
  }, []);

  return (
        <div style={{height: 600, marginTop: '40px', paddingBottom: '100px'}}>
          <h3>Orders</h3>
          <DataGrid
              rows={rows}
              columns={columns as any}
              editMode="row"
              rowModesModel={rowModesModel}
              onRowModesModelChange={(newModel) => handleRowModesModelChange(newModel, setRowModesModel)}
              slots={{
                toolbar: ToolbarOrders,
              }}
              slotProps={{
                toolbar: {refetch},
              }}
              rowHeight={35}
              autoPageSize
          />
        </div>

  );
};

export default OrdersPage;

