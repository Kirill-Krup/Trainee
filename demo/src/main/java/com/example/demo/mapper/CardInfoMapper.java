  package com.example.demo.mapper;

  import com.example.demo.dto.CardInfoDTO;
  import com.example.demo.dto.CreateCardInfoDTO;
  import com.example.demo.model.CardInfo;
  import org.mapstruct.Mapper;
  import org.mapstruct.Mapping;
  import org.mapstruct.factory.Mappers;
  import org.springframework.stereotype.Component;

  @Component
  @Mapper(componentModel = "spring")
  public interface CardInfoMapper {

    CardInfoMapper INSTANCE = Mappers.getMapper(CardInfoMapper.class);

    CardInfoDTO toDTO(CardInfo cardInfo);

    CardInfo toEntity(CardInfoDTO cardInfoDTO);

    CreateCardInfoDTO toDTOCreateCardInfoDTO(CardInfo cardInfo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CardInfo toEntityForCreate(CreateCardInfoDTO createCardInfoDTO);
  }
