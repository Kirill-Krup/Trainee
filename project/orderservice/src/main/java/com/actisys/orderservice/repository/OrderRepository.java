package com.actisys.orderservice.repository;

import com.actisys.orderservice.model.Order;
import com.actisys.orderservice.model.enumClasses.StatusType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByIdIn(List<Long> ids);

  List<Order> findByStatusIn(List<StatusType> types);

}
