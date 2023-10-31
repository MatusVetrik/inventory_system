package com.vetrikos.inventory.system.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.proxy.HibernateProxy;


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
    private Integer capacity;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<ItemListEntry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "fromWarehouse", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<Order> sendOrders = new ArrayList<>();

    @OneToMany(mappedBy = "toWarehouse", fetch = FetchType.EAGER)
    @Builder.Default
    @ToString.Exclude
    private List<Order> receivedOrders = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER, cascade = {
        CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH
    })
    @Builder.Default
    @BatchSize(size = 10)
    private List<User> users = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass = o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        Warehouse warehouse = (Warehouse) o;
        return getId() != null && Objects.equals(getId(), warehouse.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
