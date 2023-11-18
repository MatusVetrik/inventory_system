package com.vetrikos.inventory.system.mapper;

import com.vetrikos.inventory.system.entity.Item;
import com.vetrikos.inventory.system.model.WarehouseItemRequestRestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ItemMapperImpl.class,
})
class ItemMapperTest {

    @Autowired
    private ItemMapper itemMapper;


    @Test
    void warehouseItemRequestRestDTOToItem() {
        long size = 500;
        String name = "Apple";
        long quantity = 30;
        WarehouseItemRequestRestDTO requestRestDTO = new WarehouseItemRequestRestDTO(name, size, quantity);

        Item expectedItem = Item.builder()
                .name(name)
                .size(size)
                .build();

        Item result = itemMapper.warehouseItemRequestRestDTOToItem(requestRestDTO);

        assertThat(result).isNotNull()
                .isEqualTo(expectedItem);
    }
}