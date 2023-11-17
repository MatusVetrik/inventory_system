import {ClientResponse} from "../model/ClientResponse.ts";
import {Configuration, User, WarehouseUsersApi} from "inventory-client-ts-axios";
import {axiosInstance} from "./axiosInstance.ts";

const client = new WarehouseUsersApi(new Configuration(), '', axiosInstance);

export const getWarehouseUsers = async (warehouseId: number): Promise<ClientResponse<User[]>> => {
    const {data, status} = await client.getWarehouseUsers(warehouseId);
    return {data, responseCode: status};
};
export const addWarehouseUser = async (warehouseId: number, userId: string): Promise<ClientResponse<void>> => {
    const {data, status} = await client.addWarehouseUser(warehouseId, userId);
    return {data, responseCode: status};
};

export const getWarehouseUserById = async (warehouseId: number, userId: string): Promise<ClientResponse<User>> => {
    const {data, status} = await client.getWarehouseUserById(warehouseId, userId);
    return {data, responseCode: status};
};

export const deleteWarehouseUser = async (warehouseId: number, userId: string): Promise<ClientResponse<void>> => {
    const {status} = await client.removeWarehouseUser(warehouseId, userId);
    return {responseCode: status};
};


