import {ClientResponse} from "../model/ClientResponse.ts";

export const getListOrders = async (): Promise<ClientResponse<void>> => {
    return {responseCode: 200};
};
export const createOrder = async (): Promise<void> => {
};

export const getOrderById = async (): Promise<void> => {
};

export const deleteOrder = async (): Promise<void> => {
};


