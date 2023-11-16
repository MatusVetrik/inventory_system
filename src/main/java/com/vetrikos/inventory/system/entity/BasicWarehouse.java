package com.vetrikos.inventory.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class BasicWarehouse {

  private Long id;

  private Integer capacity;

  private String name;

  private Long itemsCapacitySize;
}
