package com.actisys.paymentservice.repository;

import com.actisys.paymentservice.model.Payment;
import com.actisys.paymentservice.model.enums.PaymentStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

  List<Payment> findByOrderId(Long orderId);

  List<Payment> findByUserId(Long userId);

  List<Payment> findByStatusIn(List<PaymentStatus> statuses);

  @Aggregation(pipeline = {
      "{$match: {timestamp: {$gte: ?0, $lte: ?1}}}",
      "{$group: {_id: null, total: {$sum: {$toDouble: '$payment_amount'}}}}"
  })
  Double getTotalSumForPeriod(Instant from, Instant to);
}
