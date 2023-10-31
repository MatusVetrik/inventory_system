package com.vetrikos.inventory.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @OneToMany(mappedBy = "order")
  @Builder.Default
  private List<ItemListEntry> entries = new ArrayList<>();

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
    if (!entries.equals(order.entries)) {
      return false;
    }
    return createdBy.equals(order.createdBy);
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + entries.hashCode();
    result = 31 * result + createdBy.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", entries=" + entries +
        ", createdBy=" + createdBy +
        ", fromWarehouse=" + fromWarehouse +
        ", toWarehouse=" + toWarehouse +
        '}';
  }
}
