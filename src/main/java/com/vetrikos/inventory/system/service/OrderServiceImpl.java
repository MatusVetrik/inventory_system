package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.Order;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.exception.ItemExceedsWarehouseCapacityException;
import com.vetrikos.inventory.system.model.OrderRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ItemListEntryRepository itemListEntryRepository;
  private final UserService userService;
  private final ItemService itemService;
  private final WarehouseService warehouseService;

  @Override
  @NonNull
  public List<Order> findAllOrders() {
    return orderRepository.findAll();
  }

  @Override
  @NonNull
  @Transactional
  public Order createOrder(OrderRestDTO requestRestDTO) {
    String principalUuid = getPrincipalUuid();
    Warehouse sourceWarehouse = warehouseService.findById(requestRestDTO.getSourceId());
    Warehouse destinationWarehouse = warehouseService.findById(requestRestDTO.getDestinationId());
    Item itemInSourceWarehouse = itemService.findItemInWarehouse(requestRestDTO.getSourceId(),
        requestRestDTO.getItemId());
    Long quantity = requestRestDTO.getQuantity();

    validateEnoughItemForOrder(sourceWarehouse, itemInSourceWarehouse, quantity);
    validateCapacityForOrder(destinationWarehouse, itemInSourceWarehouse.getSize(), quantity);

    updateSourceWarehouseEntries(sourceWarehouse, itemInSourceWarehouse, quantity);
    updateDestinationWarehouseEntries(destinationWarehouse, itemInSourceWarehouse, quantity);

    Order order = Order.builder()
        .createdBy(userService.findById(UUID.fromString(principalUuid)))
        .item(itemInSourceWarehouse)
        .quantity(requestRestDTO.getQuantity())
        .fromWarehouse(warehouseService.findById(requestRestDTO.getSourceId()))
        .toWarehouse(warehouseService.findById(requestRestDTO.getDestinationId()))
        .build();

    return orderRepository.save(order);  }

  private void validateEnoughItemForOrder(Warehouse sourceWarehouse, Item itemInSourceWarehouse, Long quantity) {
    List<ItemListEntry> existingItemSource = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
        sourceWarehouse, itemInSourceWarehouse);

    if (existingItemSource.isEmpty()) {
      throw new IllegalStateException("No item found in source warehouse");
    }

    ItemListEntry itemListEntrySource = existingItemSource.get(0);

    if (itemListEntrySource.getQuantity() < quantity) {
      throw new ItemExceedsWarehouseCapacityException(quantity - itemListEntrySource.getQuantity(),
          sourceWarehouse.getId());
    }
  }

  private String getPrincipalUuid() {
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return jwt.getSubject();
  }

  private void validateCapacityForOrder(Warehouse destinationWarehouse, Long itemSize,
      Long quantity) {
    Long itemsCapacity = itemSize * quantity;
    Long currentCapacityDestination = calculateCurrentCapacity(destinationWarehouse);

    if (itemsCapacity + currentCapacityDestination > destinationWarehouse.getCapacity()) {
      throw new ItemExceedsWarehouseCapacityException(
          (itemsCapacity + currentCapacityDestination) - destinationWarehouse.getCapacity());
    }
  }

  private void updateSourceWarehouseEntries(Warehouse sourceWarehouse, Item item, Long quantity) {
    ItemListEntry itemListEntrySource = findItemListEntryInWarehouse(sourceWarehouse, item);

    itemListEntrySource.setQuantity(itemListEntrySource.getQuantity() - quantity);
    if (itemListEntrySource.getQuantity() == 0) {
      removeAndDeleteItemListEntry(sourceWarehouse, itemListEntrySource);
    } else {
      itemListEntryRepository.save(itemListEntrySource);
    }
  }

  private void updateDestinationWarehouseEntries(Warehouse destinationWarehouse, Item item,
      Long quantity) {
    List<ItemListEntry> existingItemDestination = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
        destinationWarehouse, item);

    if (existingItemDestination.isEmpty()) {
      ItemListEntry newItemListEntry = ItemListEntry.builder().item(item)
          .warehouse(destinationWarehouse).quantity(quantity).build();
      itemListEntryRepository.save(newItemListEntry);
    } else {
      ItemListEntry itemListEntry = existingItemDestination.get(0);
      itemListEntry.setQuantity(itemListEntry.getQuantity() + quantity);
      itemListEntryRepository.save(itemListEntry);
    }
  }

  private ItemListEntry findItemListEntryInWarehouse(Warehouse warehouse, Item item) {
    return itemListEntryRepository.findItemListEntriesByWarehouseAndItem(warehouse, item)
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No item found in warehouse"));
  }

  private void removeAndDeleteItemListEntry(Warehouse warehouse, ItemListEntry itemListEntry) {
    warehouse.getEntries().remove(itemListEntry);
    itemListEntry.setWarehouse(null);
    itemListEntryRepository.delete(itemListEntry);
  }


  public Long calculateCurrentCapacity(Warehouse warehouse) {
    Long currentCapacity = 0L;

    List<ItemListEntry> itemList = warehouse.getEntries();
    for (ItemListEntry entry : itemList) {
      Long itemSize = entry.getItem().getSize();
      Long itemQuantity = entry.getQuantity();
      Long totalItemSize = itemSize * itemQuantity;
      currentCapacity += totalItemSize;
    }
    return currentCapacity;
  }

}
