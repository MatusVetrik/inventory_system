import {ReactElement} from "react";
import {DataGrid, GridRowParams, GridToolbar} from '@mui/x-data-grid';
import * as React from "react";
import routes from "../../routing/routes";
import {useNavigate} from "react-router-dom";

const columns = [
    { field: 'id', headerName: 'ID', width: 100 },
    { field: 'capacity', headerName: 'Capacity', width: 200 },
    { field: 'location', headerName: 'Location', width: 200 },
];

function createData(
    id: number,
    capacity: number,
    location:string

) {
    return { id:id, capacity:capacity,location:location };
}

const rows = [
    createData(1, 1235,"Bratislava"),
    createData(2, 326,"Lozorno"),
    createData(3, 1588,"Prague"),
    createData(4, 1236,"Vienna"),
    createData(5, 5668,"Budapest"),
    createData(6, 84989,"Brno"),

];

const WarehousePage = () => {
    const navigate = useNavigate();
    function handleRowClick(row: GridRowParams<any>) {
        const rowData = rows.find(r => r.id === row.id);
        navigate(routes.items, { state: { rowDetails: rowData } });
    }
    return (
        <div>
            <h1>Warehouses</h1>
            <DataGrid
                slots={{toolbar: GridToolbar}}
                rows={rows}
                columns={columns}
                onRowClick={(row) => handleRowClick(row)}
                style={{ cursor: 'pointer' }}
                rowHeight={35}
                headerHeight={40}

            />
        </div>

    )
}
export default WarehousePage;