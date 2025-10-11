package com.actisys.orderservice.mapper;

import com.actisys.orderservice.dto.OrderItemDto;
import com.actisys.orderservice.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface OrderItemMapper {

  OrderItemDto toDto(OrderItem orderItem);

  OrderItem toEntity(OrderItemDto orderItemDto);

  List<OrderItemDto> toDto(List<OrderItem> orderItemList);
}
