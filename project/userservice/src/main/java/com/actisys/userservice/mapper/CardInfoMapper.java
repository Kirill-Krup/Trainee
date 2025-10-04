  package com.actisys.userservice.mapper;

  import com.actisys.userservice.dto.CardInfoDTO;
  import com.actisys.userservice.dto.CreateCardInfoDTO;
  import com.actisys.userservice.model.CardInfo;
  import org.mapstruct.Mapper;
  import org.mapstruct.Mapping;
  import org.mapstruct.factory.Mappers;
  import org.springframework.stereotype.Component;

  @Component
  @Mapper(componentModel = "spring")
  public interface CardInfoMapper {

    CardInfoDTO toDTO(CardInfo cardInfo);

    CardInfo toEntity(CardInfoDTO cardInfoDTO);

    CreateCardInfoDTO toCreateDTO(CardInfo cardInfo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CardInfo toEntityForCreate(CreateCardInfoDTO createCardInfoDTO);

  }
