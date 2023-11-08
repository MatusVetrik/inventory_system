import React from "react";
import {GridActionsCellItem, GridRowModes, GridRowModesModel} from "@mui/x-data-grid";
import SaveIcon from "@mui/icons-material/Save";
import {handleEditClick, handleSaveClick} from "../../functions/handlers.ts";
import CancelIcon from "@mui/icons-material/Close";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/DeleteOutlined";

interface Props {
    id: number,
    rowModesModel: GridRowModesModel,
    setRowModesModel: React.Dispatch<React.SetStateAction<GridRowModesModel>>,
    handleCancelClick: (id: number) => () => void,
    handleDeleteClick: (id: number) => () => Promise<void>

}

export default ({id, rowModesModel, setRowModesModel, handleCancelClick, handleDeleteClick}: Props) => {
    const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

    if (isInEditMode) {
        return [
            <GridActionsCellItem
                icon={<SaveIcon/>}
                label="Save"
                sx={{
                    color: 'primary.main',
                }}
                onClick={handleSaveClick(id, setRowModesModel)}
            />,
            <GridActionsCellItem
                icon={<CancelIcon/>}
                label="Cancel"
                className="textPrimary"
                onClick={handleCancelClick(id)}
                color="inherit"
            />,
        ];
    }

    return [
        <GridActionsCellItem
            icon={<EditIcon/>}
            label="Edit"
            className="textPrimary"
            onClick={handleEditClick(id, setRowModesModel)}
            color="inherit"
        />,
        <GridActionsCellItem
            icon={<DeleteIcon/>}
            label="Delete"
            onClick={handleDeleteClick(id)}
            color="inherit"
        />,
    ];
}