package com.actisys.orderservice.mapper;

import com.actisys.orderservice.dto.ItemDTO;
import com.actisys.orderservice.model.Item;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

  ItemDTO toDTO(Item item);

  Item toEntity(ItemDTO itemDTO);

  List<ItemDTO> toDTO(List<Item> itemList);
}
