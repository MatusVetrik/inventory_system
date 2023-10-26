package com.vetrikos.inventory.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "ITEM_LIST_ENTRY")
public class ItemListEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
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
