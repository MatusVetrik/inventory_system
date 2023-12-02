import {ClientResponse} from "../model/ClientResponse.ts";
import {Configuration, Order, OrderResponse, OrdersApi} from "inventory-client-ts-axios";
import {axiosInstance} from "./axiosInstance.ts";

const client = new OrdersApi(new Configuration(), '', axiosInstance);

export const getListOrders = async (): Promise<ClientResponse<OrderResponse[]>> => {
    const {data, status} = await client.listOrders();
    return {data, responseCode: status}
};
export const createOrder = async (order: Order): Promise<ClientResponse<OrderResponse>> => {
    const {data, status} = await client.createOrder(order)
    return {data, responseCode: status};
};


