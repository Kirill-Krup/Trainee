package com.actisys.orderservice.controller;

import com.actisys.orderservice.dto.OrderDTO;
import com.actisys.orderservice.model.enumClasses.StatusType;
import com.actisys.orderservice.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
    OrderDTO createdDto = orderService.createOrder(orderDTO);
    return ResponseEntity.ok().body(createdDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/by-ids")
  public ResponseEntity<List<OrderDTO>> getOrdersByIds(@RequestParam("ids") List<Long> ids) {
    return ResponseEntity.ok(orderService.getOrdersByIdIn(ids));
  }

  @GetMapping("/by-status")
  public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@RequestParam("statuses") List<StatusType> statuses) {
    return ResponseEntity.ok(orderService.getOrdersByStatusIn(statuses));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
      OrderDTO updatedDto = orderService.updateOrder(id, orderDTO);
      return ResponseEntity.ok(updatedDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}
