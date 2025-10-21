package com.actisys.orderservice.dto;

import com.actisys.orderservice.model.enumClasses.StatusType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {

  private final Long id;

  @NotNull(message = "Can't create order without user")
  private Long userId;

  @NotNull(message = "Status is required")
  private final StatusType status;

  private final LocalDateTime creationDate;

  private final List<OrderItemDto> orderItems;
}
