package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.entity.ItemListEntry;
import com.vetrikos.inventory.system.model.WarehouseItemRestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ItemListEntryMapperImpl.class
})
class ItemListEntryMapperTest {

    @Autowired
    private ItemListEntryMapper itemListEntryMapper;

    @Test
    void itemListEntryToWarehouseItemRestDTO() {
        long size = 100;
        String name = "Kebab";
        Item item = Item.builder()
                .id(1L)
                .size(size)
                .name(name)
                .build();

        Long itemListEntryId = 1L;
        long quantity = 70;
        ItemListEntry itemListEntry = ItemListEntry.builder()
                .id(itemListEntryId)
                .item(item)
                .quantity(quantity)
                .build();

        WarehouseItemRestDTO expectedResult = new WarehouseItemRestDTO(itemListEntryId, name, size, quantity);

        WarehouseItemRestDTO result = itemListEntryMapper.itemListEntryToWarehouseItemRestDTO(
                itemListEntry);

        assertThat(result).isNotNull()
                .isEqualTo(expectedResult);
    }
}