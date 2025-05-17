package org.fcup.model.dto.mapper;

import org.fcup.model.auction.Item;
import org.fcup.model.dto.ItemDTO;

public class ItemMapper {
    public static Item convertToItem(ItemDTO itemDTO) {
        return new Item(itemDTO.minimumValue(), itemDTO.name(), UserMapper.convertToUser(itemDTO.owner()));
    }

    public static ItemDTO convertToItemDTO(Item item) {
        return new ItemDTO(item.getName(), item.getMinimumValue(), UserMapper.convertToUserDTO(item.getOwner()));
    }
}
