
const api = new WarehousesApi();

export const addItemToWarehouse = async (warehouseId: number, itemId: number, warehouseRequest: WarehouseRequest): Promise<ClientResponse<void>> => {
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(cestovnyPrikaz),
    };
    const response = await fetch(
        'http://localhost:8080/api/ulohy/cestovny-prikaz',
        requestOptions,
    );
    return {responseCode: response?.status};
};

