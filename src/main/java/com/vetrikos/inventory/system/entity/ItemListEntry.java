package com.vetrikos.inventory.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

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
  private Integer quantity;

  @ManyToOne()
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne()
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne()
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

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
    ItemListEntry that = (ItemListEntry) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return getClass().hashCode();
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
