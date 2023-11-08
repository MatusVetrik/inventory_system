import {ClientResponse} from "../model/ClientResponse.ts";
import {WarehouseItem, WarehouseItemRequest, WarehouseItemsApi} from "inventory-client-ts-axios";

const client = new WarehouseItemsApi();

export const getListWarehouseItems = async (warehouseId: number): Promise<ClientResponse<WarehouseItem[]>> => {
    const {data, status} = await client.listWarehouseItems(warehouseId);
    return {data, responseCode: status};
};
export const createWarehouseItem = async (warehouseId: number, item: WarehouseItemRequest): Promise<ClientResponse<WarehouseItem>> => {
    const {data, status} = await client.createWarehouseItem(warehouseId, item);
    return {data, responseCode: status};
};

export const getWarehouseItemById = async (warehouseId: number, itemId: number): Promise<ClientResponse<WarehouseItem>> => {
    const {data, status} = await client.getWarehouseItemById(warehouseId, itemId);
    return {data, responseCode: status};
};

export const updateWarehouseItem = async (warehouseId: number, itemId: number, item: WarehouseItemRequest): Promise<ClientResponse<WarehouseItem>> => {
    const {data, status} = await client.updateWarehouseItem(warehouseId, itemId, item);
    return {data, responseCode: status};
};

export const deleteWarehouseItem = async (warehouseId: number, itemId: number): Promise<ClientResponse<void>> => {
    const {status} = await client.deleteWarehouseItem(warehouseId, itemId);
    return {responseCode: status};
};


