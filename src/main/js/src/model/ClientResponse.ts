import {ClientStatusError} from './ClientStatusError';

export interface ClientResponse<T> {
    data?: T;
    responseCode: number;
    errors?: ClientStatusError[];
}
