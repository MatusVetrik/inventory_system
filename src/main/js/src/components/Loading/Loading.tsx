import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {CircularProgress} from "@mui/material";

export default () => {
  return (
      <Box justifyContent='center'
           alignItems='center'>
        <CircularProgress/>
        <Typography>Loading...</Typography>
      </Box>
  );
}