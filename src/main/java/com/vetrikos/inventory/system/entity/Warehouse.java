package com.vetrikos.inventory.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "WAREHOUSES")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "warehouseId")
    private List<ItemListEntry> entries;

    @OneToMany(mappedBy = "fromWarehouse")
    private List<Order> sendOrders;

    @OneToMany(mappedBy = "toWarehouse")
    private List<Order> receivedOrders;

    @OneToMany(mappedBy = "warehouse")
    private List<User> users;
}
