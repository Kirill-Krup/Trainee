package com.actisys.orderservice.dto;

import com.actisys.orderservice.model.enumClasses.StatusType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDTO {

  private final Long id;

  @NotNull(message = "Can't create order without user")
  private final UserDTO user;

  @NotNull(message = "Status is required")
  private final StatusType status;

  private final LocalDateTime creationDate;
}
