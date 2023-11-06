import * as React from 'react';

import {useLocation, useNavigate} from "react-router-dom";


const ItemsPage = (props) => {
    const location = useLocation();
    const rowDetails = location.state?.rowDetails;

    return (
        <div>
            <h2>Items for warehouse no.{rowDetails.id}</h2>
            {rowDetails && (
                <div>
                    <p>ID: {rowDetails.id}</p>
                </div>
            )}
        </div>
    );

};

export default ItemsPage;

