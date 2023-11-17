import {ReactElement, useEffect, useState} from "react";
import {useKeycloak} from "@react-keycloak/web";
import {Avatar, CircularProgress, List, ListItem, ListItemIcon, ListItemText, Paper} from "@mui/material";
import Typography from "@mui/material/Typography";
import Person4Icon from '@mui/icons-material/Person4';
import ArrowRightAltIcon from '@mui/icons-material/ArrowRightAlt';


export default (): ReactElement => {

    const [userData, setUserData] = useState<any>();

    const {keycloak} = useKeycloak();

    useEffect(() => {
        keycloak.loadUserInfo().then(() => {
            console.log(keycloak.userInfo)
            setUserData(keycloak.userInfo)
        })
    }, []);

    return (
        userData ?
            <Paper elevation={3}
                   style={{
                       padding: 40,
                       maxWidth: 400,
                       margin: 'auto',
                       borderRadius: 10,
                   }}>
                <Avatar sx={{width: 100, height: 100, margin: 'auto'}}>
                    <Person4Icon style={{fontSize: 80}}/>
                </Avatar>
                <div style={{display: 'flex', width: '100%', flexDirection: 'column', gap: '40px', marginTop: '40px'}}>
                    <div style={{flex: 1}}>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <Typography variant="body1"><b>Name</b></Typography>
                            <Typography variant="body1">{userData?.name}</Typography>
                        </div>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <Typography variant="body1"><b>Username</b></Typography>
                            <Typography variant="body1">{userData?.preferred_username}</Typography>
                        </div>
                    </div>
                    <div style={{flex: 1}}>
                        <Typography variant="body1">
                            <b>Roles</b>
                            <List dense={false}>
                                {userData?.roles?.map((el: string, i: number) => (
                                    <ListItem key={i}>
                                        <ListItemIcon>
                                            <ArrowRightAltIcon/>
                                        </ListItemIcon>
                                        <ListItemText
                                            primary={el}
                                        />
                                    </ListItem>
                                ))}
                            </List>
                        </Typography>
                    </div>
                </div>
            </Paper> : <h1><CircularProgress/></h1>
    )
}