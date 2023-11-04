package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.ItemRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;
  private final WarehouseRepository warehouseRepository;
  private final ItemListEntryRepository itemListEntryRepository;

  public ItemServiceImpl(ItemRepository itemRepository, WarehouseRepository warehouseRepository,
      ItemListEntryRepository itemListEntryRepository) {
    this.itemRepository = itemRepository;
    this.warehouseRepository = warehouseRepository;
    this.itemListEntryRepository = itemListEntryRepository;

  }

  @Override
  @NonNull
  public Item findById(Long itemId) {
    return itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException(
            ItemService.itemNotFoundMessage(itemId)));
  }

  @NonNull
  @Override
  public List<Item> findAll() {
    return itemRepository.findAll();
  }

  @NonNull
  @Override
  public List<Item> findItemsInWarehouse(Long warehouseId) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new IllegalArgumentException(
            WarehouseService.warehouseNotFoundMessage(warehouseId)));
    List<ItemListEntry> itemListEntries = itemListEntryRepository.findItemListEntriesByWarehouse(
        warehouse);
    if (itemListEntries.isEmpty()) {
      return new ArrayList<>();
    }

    List<Item> itemsInWarehouse = new ArrayList<>();
    for (ItemListEntry itemListEntry : itemListEntries
    ) {
      itemsInWarehouse.add(itemListEntry.getItem());
    }

    return itemsInWarehouse;
  }

  @NonNull
  @Override
  public Item createItem(Long warehouseId, WarehouseItemRestDTO requestRestDTO) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new IllegalArgumentException(
            WarehouseService.warehouseNotFoundMessage(warehouseId)));

    Item newItem = new Item();
    newItem.setName(requestRestDTO.getName());
    newItem.setSize(requestRestDTO.getSize());

    // newItem = itemRepository.save(newItem);
    ItemListEntry itemListEntry = ItemListEntry.builder().item(newItem).warehouse(warehouse)
        .quantity(
            requestRestDTO.getQuantity()).build();
    newItem.setEntries(new ArrayList<>(List.of(itemListEntry)));

    return itemRepository.save(newItem);
  }

  @NonNull
  @Override
  @Transactional
  public Item updateItem(Long itemId, Long warehouseId,
      WarehouseItemRequestRestDTO updateRequestRestDTO) {
    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new IllegalArgumentException(

            WarehouseService.warehouseNotFoundMessage(warehouseId)));
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException(
            ItemService.itemNotFoundMessage(itemId)));

    ItemListEntry existingItem = itemListEntryRepository.findItemListEntryByWarehouseAndItem(
        warehouse, item);
    //existingItem.setQuantity(updateRequestRestDTO.ge);
    Item itemInWarehouse = existingItem.getItem();
    itemInWarehouse.setName(updateRequestRestDTO.getName());
    itemInWarehouse.setSize(updateRequestRestDTO.getSize());

    // Update other properties if needed

    return itemRepository.save(itemInWarehouse);
  }

  @Override
  @Transactional
  public void deleteItem(Long itemId) {
    Item existingItem = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException(
            ItemService.itemNotFoundMessage(itemId)));

    itemRepository.delete(existingItem);
  }
}