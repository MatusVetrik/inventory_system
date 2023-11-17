import {ReactNode} from 'react';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import LogoutIcon from '@mui/icons-material/Logout';
import {Container, Grid, Tooltip} from "@mui/material";
import {useNavigate} from "react-router-dom";
import AppBar from "./components/AppBar";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import routes from "../../routing/routes";
import {useKeycloak} from "@react-keycloak/web";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import WarehouseIcon from '@mui/icons-material/Warehouse';

export const drawerWidth: number = 240;

interface Props {
    children: ReactNode
}

// TODO remove, this demo shouldn't need to reset the theme.
const defaultTheme = createTheme();

export default ({children}: Props) => {
    const navigate = useNavigate();

    const {keycloak} = useKeycloak();
    const onClickLogout = () => keycloak.logout();

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{display: 'flex'}}>
                <CssBaseline/>
                <AppBar position="fixed" open={false}>
                    <Toolbar sx={{pr: '24px',}}>
                        <Tooltip title="Orders">
                            <IconButton color="inherit" onClick={() => navigate(routes.orders)}>
                                <ShoppingCartIcon fontSize="large"/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Warehouses">
                            <IconButton color="inherit" onClick={() => navigate(routes.warehouses)}>
                                <WarehouseIcon fontSize="large"/>
                            </IconButton>
                        </Tooltip>
                        <Typography
                            component="h1"
                            variant="h4"
                            color="inherit"
                            noWrap
                            sx={{flexGrow: 1}}
                        >
                            Inventory system
                        </Typography>
                        <Tooltip title="Profile">
                            <IconButton color="inherit" onClick={() => navigate(routes.profile)}>
                                <AccountCircleIcon fontSize="large"/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Log out">
                            <IconButton color="inherit" onClick={onClickLogout}>
                                <LogoutIcon fontSize="large"/>
                            </IconButton>
                        </Tooltip>
                    </Toolbar>
                </AppBar>
                <Box component="main" sx={{flexGrow: 1, p: 3}}>
                    <Container maxWidth="lg" sx={{mt: 4, mb: 4}}>
                        <Grid alignItems="center"
                              justifyContent="center"
                              container spacing={3}>
                            <Grid item xs={12} md={8} lg={9}>
                                {children}
                            </Grid>
                        </Grid>
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}