package com.vetrikos.inventory.system.service;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.entity.Warehouse;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import com.vetrikos.inventory.system.repository.ItemListEntryRepository;
import com.vetrikos.inventory.system.repository.ItemRepository;
import com.vetrikos.inventory.system.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

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

        Set<Item> itemsInWarehouse = new HashSet<>();
        for (ItemListEntry itemListEntry : itemListEntries
        ) {
            itemsInWarehouse.add(itemListEntry.getItem());
        }

        return new ArrayList<>(itemsInWarehouse);
    }

    @NonNull
    @Override
    public Item findItemInWarehouse(Long warehouseId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        ItemService.itemNotFoundMessage(itemId)));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        WarehouseService.warehouseNotFoundMessage(warehouseId)));
        List<ItemListEntry> existingItem = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
                warehouse, item);
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException(
                    ItemListEntryService.itemInWarehouseNotFoundMessage(itemId, warehouseId));
        }

        return existingItem.get(0).getItem();
    }

    @NonNull
    @Override
    @Transactional
    public Item createItem(Long warehouseId, WarehouseItemRequestRestDTO requestRestDTO) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        WarehouseService.warehouseNotFoundMessage(warehouseId)));

        Long quantity = requestRestDTO.getQuantity();
        Long itemCapacity = quantity * requestRestDTO.getSize();
        Long currentCapacity = calculateCurrentCapacity(warehouse);

        if (itemCapacity + currentCapacity > warehouse.getCapacity()) {
            throw new IllegalArgumentException(ItemService.itemExceedsCapacityMessage(
                    (itemCapacity + currentCapacity) - warehouse.getCapacity()));
        }

        Item newItem = new Item();
        newItem.setName(requestRestDTO.getName());
        newItem.setSize(requestRestDTO.getSize());
        ItemListEntry itemListEntry = ItemListEntry.builder().item(newItem).warehouse(warehouse)
                .quantity(
                        requestRestDTO.getQuantity()).build();
        newItem.setEntries(new ArrayList<>(List.of(itemListEntry)));
        Item savedItem = itemRepository.save(newItem);
        itemListEntry = itemListEntryRepository.save(itemListEntry);
        warehouse = warehouseRepository.save(warehouse);
        return savedItem;
    }

    @NonNull
    @Override
    @Transactional
    public Item updateItem(Long itemId, Long warehouseId,
                           WarehouseItemRequestRestDTO updateRequestRestDTO) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        WarehouseService.warehouseNotFoundMessage(warehouseId)));
        Item item;
        try {
            item = findItemInWarehouse(warehouseId, itemId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ItemListEntryService.itemInWarehouseNotFoundMessage(itemId, warehouseId));
        }

        List<ItemListEntry> existingItem = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
                warehouse, item);
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException(ItemListEntryService.itemInWarehouseNotFoundMessage(itemId, warehouseId));
        }

        ItemListEntry itemListEntry = existingItem.get(0);
        Item itemInWarehouse = existingItem.get(0).getItem();
        Long currentItemCapacity = itemInWarehouse.getSize() * itemListEntry.getQuantity();

        Long quantity = updateRequestRestDTO.getQuantity();
        Long itemCapacity = quantity * updateRequestRestDTO.getSize();
        Long currentWarehouseCapacity = calculateCurrentCapacity(warehouse);
        if (itemCapacity - currentItemCapacity + currentWarehouseCapacity > warehouse.getCapacity()) {
            throw new IllegalArgumentException(ItemService.itemExceedsCapacityMessage(
                    (itemCapacity - currentItemCapacity + currentWarehouseCapacity) - warehouse.getCapacity()));
        }
        itemListEntry.setQuantity(quantity);
        itemInWarehouse.setName(updateRequestRestDTO.getName());
        itemInWarehouse.setSize(updateRequestRestDTO.getSize());
        itemListEntryRepository.save(itemListEntry);
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

    @Override
    @Transactional
    public void deleteWarehouseItem(Long itemId, Long warehouseId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException(
                        ItemService.itemNotFoundMessage(itemId)));
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        WarehouseService.warehouseNotFoundMessage(warehouseId)));
        List<ItemListEntry> existingItem = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
                warehouse, item);
        if (existingItem.isEmpty()) {
            throw new IllegalArgumentException(
                    ItemListEntryService.itemInWarehouseNotFoundMessage(itemId, warehouseId));
        }
        for (ItemListEntry entry : existingItem) {
            if (Objects.equals(entry.getWarehouse().getId(), warehouseId)) {
                entry.setWarehouse(null);
                entry.setOrder(null);
                itemListEntryRepository.delete(entry);
            }
        }
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

    public Long calculateItemCapacityInWarehouse(List<ItemListEntry> entries, Long warehouseId) {
        for (ItemListEntry itemListEntry : entries) {

        }
        return 0L;
    }


}
