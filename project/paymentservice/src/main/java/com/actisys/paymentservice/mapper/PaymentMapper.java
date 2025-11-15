package com.actisys.paymentservice.mapper;

import com.actisys.paymentservice.dto.PaymentCreateDTO;
import com.actisys.paymentservice.dto.PaymentDTO;
import com.actisys.paymentservice.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  Payment toEntity(PaymentDTO dto);

  PaymentDTO toDto(Payment entity);

  Payment toEntity(PaymentCreateDTO dto);
}
