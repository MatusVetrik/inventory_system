import {useCallback, useEffect} from 'react';
import useClient from './useClient';
import {ClientStatus} from "../model/ClientStatus.ts";
import {ClientResponse} from "../model/ClientResponse.ts";

interface ReturnType<T> {
    data: T | null;
    status: ClientStatus;

    wrapClient(): void;

    refetch(): void;
}

type ClientCall<T> = (...args: any) => Promise<ClientResponse<T>>;

export default <T>(client: ClientCall<T>, deps = []): ReturnType<T> => {
    const {
        wrapClient, data, status,
    } = useClient<T>(client);

    useEffect(() => {
        wrapClient()
            .catch(() => { /* noop */
            });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, deps);

    const refetch = useCallback(() => {
        wrapClient()
            .catch(() => { /* noop */
            });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, deps);

    return {
        wrapClient, data, status, refetch,
    };
};
