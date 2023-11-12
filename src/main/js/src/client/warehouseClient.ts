import {ClientResponse} from "../model/ClientResponse.ts";
import {
    BasicWarehouse, Configuration,
    FullWarehouse,
    WarehouseRequest,
    WarehousesApi,
    WarehouseUpdateRequest
} from "inventory-client-ts-axios";
import {axiosInstance} from "./axiosInstance.ts";

const client = new WarehousesApi(new Configuration(), '', axiosInstance);

export const getListWarehouses = async (): Promise<ClientResponse<BasicWarehouse[]>> => {
    const {data, status} = await client.listWarehouses();
    return {data, responseCode: status};
};
export const createWarehouse = async (warehouse: WarehouseRequest): Promise<ClientResponse<FullWarehouse>> => {
    const {data, status} = await client.createWarehouse(warehouse);
    return {data, responseCode: status};
};

export const getWarehouseById = async (warehouseId: number): Promise<ClientResponse<FullWarehouse>> => {
    const {data, status} = await client.getWarehouseById(warehouseId);
    return {data, responseCode: status};
};

export const updateWarehouse = async (warehouseId: number, warehouse: WarehouseUpdateRequest): Promise<ClientResponse<FullWarehouse>> => {
    const {data, status} = await client.updateWarehouse(warehouseId, warehouse);
    return {data, responseCode: status};
};

export const deleteWarehouse = async (warehouseId: number): Promise<ClientResponse<void>> => {
    const {status} = await client.deleteWarehouse(warehouseId);
    return {responseCode: status};
};


