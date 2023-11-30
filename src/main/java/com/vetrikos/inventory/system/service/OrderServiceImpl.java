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
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Warehouse sourceWarehouse = warehouseService.findById(requestRestDTO.getSourceId());
        Warehouse destinationWarehouse = warehouseService.findById(requestRestDTO.getDestinationId());
        Item itemInSourceWarehouse = itemService.findItemInWarehouse(requestRestDTO.getSourceId(), requestRestDTO.getItemId());
        Long quantity = requestRestDTO.getQuantity();
        Long itemSize = itemService.findItemInWarehouse(requestRestDTO.getSourceId(), requestRestDTO.getItemId()).getSize();
        Long itemsCapacity = itemSize * quantity;
        Long currentCapacityDestination = calculateCurrentCapacity(destinationWarehouse);
        //check if the capacity of the destination warehouse exceeds the capacity of the source warehouse
        if (itemsCapacity + currentCapacityDestination > destinationWarehouse.getCapacity()) {
            throw new ItemExceedsWarehouseCapacityException(
                    (itemsCapacity + currentCapacityDestination) - destinationWarehouse.getCapacity());
        }
        //check if item already exists in the destination warehouse
        List<ItemListEntry> existingItemDestination = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
                destinationWarehouse, itemInSourceWarehouse);
        if (existingItemDestination.isEmpty()) { //if no, create new item list entry
            ItemListEntry newItemListEntry = new ItemListEntry();
            newItemListEntry.setItem(itemInSourceWarehouse);
            newItemListEntry.setQuantity(quantity);
            itemListEntryRepository.save(newItemListEntry);
        } else { //if yes, increase the quantity of the item in the destination warehouse
            ItemListEntry itemListEntry = existingItemDestination.get(0);
            itemListEntry.setQuantity(itemListEntry.getQuantity() + quantity);
            itemListEntryRepository.save(itemListEntry);
        }
        //decrease quantity of itemListEntry in source warehouse
        List<ItemListEntry> existingItemSource = itemListEntryRepository.findItemListEntriesByWarehouseAndItem(
                sourceWarehouse, itemInSourceWarehouse);
        ItemListEntry itemListEntrySource = existingItemSource.get(0);
        if (itemListEntrySource.getQuantity() - quantity < 0) { //if in source warehouse is not enough throw exception
            throw new ItemExceedsWarehouseCapacityException(itemListEntrySource.getQuantity() - quantity);
        }
        if (itemListEntrySource.getQuantity() - quantity == 0) { //if we send all items delete itemListEntry
            itemListEntryRepository.delete(itemListEntrySource);
        }
        itemListEntrySource.setQuantity(itemListEntrySource.getQuantity() - quantity);
        itemListEntryRepository.save(itemListEntrySource);

        Order order = Order.builder()
                .createdBy(userService.findById(requestRestDTO.getCreatedBy()))
                .item(itemService.findItemInWarehouse(requestRestDTO.getSourceId(), requestRestDTO.getItemId()))
                .quantity(requestRestDTO.getQuantity())
                .fromWarehouse(warehouseService.findById(requestRestDTO.getSourceId()))
                .toWarehouse(warehouseService.findById(requestRestDTO.getDestinationId()))
                .build();
        return orderRepository.save(order);
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
