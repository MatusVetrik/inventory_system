import React from "react";
import {GridEventListener, GridRowEditStopReasons, GridRowModes, GridRowModesModel} from "@mui/x-data-grid";

export const handleRowEditStop: GridEventListener<'rowEditStop'> = (params, event) => {
    if (params.reason === GridRowEditStopReasons.rowFocusOut) {
        event.defaultMuiPrevented = true;
    }
};
export const handleEditClick = (id: number, setRowModesModel: React.Dispatch<React.SetStateAction<GridRowModesModel>>) => () => {
    setRowModesModel(prev => ({...prev, [id]: {mode: GridRowModes.Edit}}));
};

export const handleSaveClick = (id: number, setRowModesModel: React.Dispatch<React.SetStateAction<GridRowModesModel>>) => () => {
    setRowModesModel(prev => ({...prev, [id]: {mode: GridRowModes.View}}));
};

export const handleRowModesModelChange = (newRowModesModel: GridRowModesModel, setRowModesModel: React.Dispatch<React.SetStateAction<GridRowModesModel>>) => {
    setRowModesModel(newRowModesModel);
};