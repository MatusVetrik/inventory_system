export default {
    index: '/app/',
    profile: '/app/profile',
    warehouses: '/app/warehouse-list',
    warehouse: {
        list: '/app/warehouse-list',
        detail: {
            raw: '/app/warehouse/:warehouseId',
            withId: (id: number): string => `/app/warehouse/${id}`
        }
    },
    orders: '/app/orders',
};
