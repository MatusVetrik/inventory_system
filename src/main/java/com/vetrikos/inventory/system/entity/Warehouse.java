package com.vetrikos.inventory.system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "WAREHOUSES")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long capacity;

    @Column(nullable = false)
    private String name;

    @Transient
    @Builder.Default
    private Long itemsCapacitySize = 0L;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
    @Builder.Default
    private List<ItemListEntry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "fromWarehouse", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<Order> sendOrders = new ArrayList<>();

    @OneToMany(mappedBy = "toWarehouse", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<Order> receivedOrders = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
    @Builder.Default
    @BatchSize(size = 10)
    private List<User> users = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse warehouse)) {
            return false;
        }

        if (!Objects.equals(id, warehouse.id)) {
            return false;
        }
        if (!capacity.equals(warehouse.capacity)) {
            return false;
        }
        return name.equals(warehouse.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + capacity.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
