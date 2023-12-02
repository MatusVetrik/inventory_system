import {useCallback, useState} from 'react';
import {ClientResponse} from '../model/ClientResponse';
import {ClientStatus} from "../model/ClientStatus.ts";
import {ClientStatusError} from "../model/ClientStatusError.ts";
import {useToast} from "../components/Toast/Toast";

interface ReturnType<T> {
    data: T | null;
    status: ClientStatus;

    wrapClient(...args: any[]): Promise<ClientResponse<T>>;
}

type ClientCall<T> = (...args: any) => Promise<ClientResponse<T>>;

export default <T>(client: ClientCall<T>): ReturnType<T> => {
    const [status, setStatus] = useState<ClientStatus>({
        failed: false,
        finished: false,
        errors: [],
        loading: false,
        responseCode: 0,
    });
    const [data, setData] = useState<T | null>(null);
    const { showToast } = useToast();

    const wrapClient = useCallback(
        async (...args: any) => {
            setStatus(prevState => ({...prevState, loading: true}));

            try {
                const response = await client(...args);

                const newStatus: ClientStatus = {
                    responseCode: response.responseCode,
                    loading: false,
                    finished: true,
                    errors: [],
                    failed: false,
                };

                setData(response.data as T);
                setStatus(newStatus);

                return Promise.resolve(response);
            } catch (e: any) {
                const {
                    data: responseData,
                    status: statusCode,
                } = e.response;

                const errors: ClientStatusError[] = [];

                responseData?.errors?.forEach((error: any) => errors.push(
                    {message: error},
                ));

                const newStatus: ClientStatus = {
                    responseCode: statusCode,
                    loading: false,
                    finished: true,
                    errors: errors,
                    failed: true,
                };

                setData(null);
                setStatus(newStatus);
                showToast(`Error: ${e.message}`, { type: 'error' });
                console.log(e.message)
                return Promise.reject();
            }
        },
        // eslint-disable-next-line react-hooks/exhaustive-deps
        [client,showToast],
    );

    return {
        wrapClient,
        data,
        status,
    };
};
