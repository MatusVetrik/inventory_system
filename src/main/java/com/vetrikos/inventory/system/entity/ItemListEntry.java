package com.vetrikos.inventory.system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ITEM_LIST_ENTRY")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ItemListEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item itemId;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order orderId;

    @ManyToOne()
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouseId;

}
