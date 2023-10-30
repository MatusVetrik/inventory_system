import {ReactElement} from "react";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, {tableCellClasses} from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {styled} from "@mui/material";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
    [`&.${tableCellClasses.head}`]: {
        backgroundColor: theme.palette.common.black,
        color: theme.palette.common.white,
    },
    [`&.${tableCellClasses.body}`]: {
        fontSize: 14,
    },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
    '&:nth-of-type(odd)': {
        backgroundColor: theme.palette.action.hover,
    },
    // hide last border
    '&:last-child td, &:last-child th': {
        border: 0,
    },
}));

function createData(
    id: number,
    capacity: number,
    location:string

) {
    return { id, capacity,location };
}

const rows = [
    createData(1, 1235,"Bratislava"),
    createData(2, 326,"Lozorno"),
    createData(3, 1588,"Prague"),
    createData(4, 1236,"Vienna"),
    createData(5, 5668,"Budapest"),
    createData(6, 84989,"Brno"),

];

export default (): ReactElement => (

    <div >
        <h1>Warehouses</h1>
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} size="small" aria-label="table">
                <TableHead>
                    <StyledTableRow>
                        <StyledTableCell>ID</StyledTableCell>
                        <StyledTableCell align="right">Capacity</StyledTableCell>
                        <StyledTableCell align="right">Location</StyledTableCell>

                    </StyledTableRow>
                </TableHead>
                <TableBody>
                    {rows.map((row) => (
                        <TableRow
                            key={row.id}
                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell component="th" scope="row">
                                {row.id}
                            </TableCell>
                            <TableCell align="right">{row.capacity}</TableCell>
                            <TableCell align="right">{row.location}</TableCell>

                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    </div>

)