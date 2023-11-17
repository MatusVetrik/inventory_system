import {ClientResponse} from "../model/ClientResponse.ts";
import {Configuration, User, UsersApi} from "inventory-client-ts-axios";
import {axiosInstance} from "./axiosInstance.ts";

const client = new UsersApi(new Configuration(), '', axiosInstance);

export const getListUsers = async (): Promise<ClientResponse<User[]>> => {
    const {data, status} = await client.listUsers();
    return {data, responseCode: status};
};

export const getUserById = async (userId: string): Promise<ClientResponse<User>> => {
    const {data, status} = await client.getUserById(userId);
    return {data, responseCode: status};
};


