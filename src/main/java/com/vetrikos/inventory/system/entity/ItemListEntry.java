package com.vetrikos.inventory.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ITEM_LIST_ENTRY")
public class ItemListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne(cascade = {
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    }, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = {
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    })
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemListEntry that)) {
            return false;
        }

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!quantity.equals(that.quantity)) {
            return false;
        }
        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + quantity.hashCode();
        result = 31 * result + item.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ItemListEntry{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", itemId=" + (item == null ? null : item.getId()) +
                ", orderId=" + (order == null ? null : order.getId()) +
                ", warehouseId=" + (warehouse == null ? null : warehouse.getId()) +
                '}';
    }
}
