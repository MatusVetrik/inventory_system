import {ClientStatusError} from "./ClientStatusError.ts";

export interface ClientStatus {
    loading: boolean;
    finished: boolean;
    responseCode: number;
    failed: boolean;
    errors: Array<ClientStatusError>;
}
