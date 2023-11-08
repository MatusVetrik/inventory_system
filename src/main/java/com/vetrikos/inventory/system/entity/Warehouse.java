package com.vetrikos.inventory.system.entity;

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
