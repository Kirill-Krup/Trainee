package com.example.demo.Mapper;

import com.example.demo.DTO.CardInfoDTO;
import com.example.demo.Model.CardInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CardInfoMapper {

  CardInfoMapper INSTANCE = Mappers.getMapper(CardInfoMapper.class);

  CardInfoDTO toDTO(CardInfo cardInfo);

  CardInfo toEntity(CardInfoDTO cardInfoDTO);
}
