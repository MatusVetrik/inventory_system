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
@Table(name = "ORDERS")
public class Order {

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

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "from_warehouse_id")
    private Warehouse fromWarehouse;

    @ManyToOne
    @JoinColumn(name = "to_warehouse_id")
    private Warehouse toWarehouse;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order order)) {
            return false;
        }

        if (!Objects.equals(id, order.id)) {
            return false;
        }
        if (!item.equals(order.item)) {
            return false;
        }
        if (!quantity.equals(order.quantity)) {
            return false;
        }
        return createdBy.equals(order.createdBy);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + item.hashCode();
        result = 31 * result + createdBy.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", entries=" + item +
                ", quantity=" + quantity +
                ", createdBy=" + createdBy +
                ", fromWarehouse=" + fromWarehouse +
                ", toWarehouse=" + toWarehouse +
                '}';
    }
}
