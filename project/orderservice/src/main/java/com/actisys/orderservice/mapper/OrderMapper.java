package com.actisys.orderservice.mapper;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.model.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

  OrderDTO toDTO(Order order);

  Order toEntity(OrderDTO orderDTO);

  List<OrderDTO> toDTO(List<Order> orders);
}
