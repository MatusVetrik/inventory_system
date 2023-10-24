package com.vetrikos.inventory.system.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "WAREHOUSE")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "capacity")
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